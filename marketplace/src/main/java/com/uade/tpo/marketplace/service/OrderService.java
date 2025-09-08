package com.uade.tpo.marketplace.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entity.basic.DigitalKey;
import com.uade.tpo.marketplace.entity.basic.Discount;
import com.uade.tpo.marketplace.entity.basic.Order;
import com.uade.tpo.marketplace.entity.basic.OrderItem;
import com.uade.tpo.marketplace.entity.basic.Product;
import com.uade.tpo.marketplace.entity.basic.User;
import com.uade.tpo.marketplace.entity.dto.create.OrderCreateDto;
import com.uade.tpo.marketplace.entity.dto.create.OrderItemCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.OrderItemResponseDto;
import com.uade.tpo.marketplace.entity.dto.response.OrderResponseDto;
import com.uade.tpo.marketplace.entity.enums.KeyStatus;
import com.uade.tpo.marketplace.entity.enums.OrderStatus;
import com.uade.tpo.marketplace.exceptions.BadRequestException;
import com.uade.tpo.marketplace.exceptions.InsufficientStockException;
import com.uade.tpo.marketplace.exceptions.ResourceNotFoundException;
import com.uade.tpo.marketplace.exceptions.UnauthorizedException;
import com.uade.tpo.marketplace.extra.mappers.OrderItemMapper;
import com.uade.tpo.marketplace.extra.mappers.OrderMapper;
import com.uade.tpo.marketplace.repository.interfaces.IDigitalKeyRepository;
import com.uade.tpo.marketplace.repository.interfaces.IDiscountRepository;
import com.uade.tpo.marketplace.repository.interfaces.IOrderItemRepository;
import com.uade.tpo.marketplace.repository.interfaces.IOrderRepository;
import com.uade.tpo.marketplace.repository.interfaces.IProductRepository;
import com.uade.tpo.marketplace.repository.interfaces.IUserRepository;
import com.uade.tpo.marketplace.service.interfaces.IDigitalKeyService;
import com.uade.tpo.marketplace.service.interfaces.IDiscountService;
import com.uade.tpo.marketplace.service.interfaces.IOrderService;
import com.uade.tpo.marketplace.service.interfaces.IUserService;

import jakarta.transaction.Transactional;

@Service
public class OrderService implements IOrderService {

    @Autowired
    private IOrderRepository orderRepository;
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IDigitalKeyRepository digitalKeyRepository;
    @Autowired
    private IOrderItemRepository orderItemRepository;
    @Autowired
    private IDiscountRepository discountRepository;
    @Autowired
    private IDiscountService discountService;
    @Autowired
    private IDigitalKeyService digitalKeyService;
    @Autowired
    private IUserService userService;

    private final OrderItemMapper orderItemMapper;
    private final OrderMapper orderMapper;

    public OrderService() {
        this.orderItemMapper = new OrderItemMapper();
        this.orderMapper = new OrderMapper();
    }


@Override
@Transactional(rollbackOn = Throwable.class)
public OrderResponseDto createOrder(OrderCreateDto dto, Long buyerId)
        throws ResourceNotFoundException, InsufficientStockException, BadRequestException {

    if (dto == null || dto.items() == null || dto.items().isEmpty()) {
        throw new BadRequestException("El pedido debe contener al menos un ítem.");
    }

    if (buyerId == null) {
        throw new BadRequestException("El pedido debe tener un comprador.");
    }

 
    User buyer = userRepository.findById(buyerId)
            .orElseThrow(() -> new ResourceNotFoundException("Comprador no encontrado (id=" + buyerId + ")."));


    Set<Long> productIds = dto.items().stream()
            .map(OrderItemCreateDto::productId)
            .collect(Collectors.toSet());

    Map<Long, Product> productMap = productRepository.findAllById(productIds).stream()
            .collect(Collectors.toMap(Product::getId, p -> p));

  
    Map<Long, Integer> requiredPerProduct = new HashMap<>();
    BigDecimal subtotal = BigDecimal.ZERO;

    for (OrderItemCreateDto itemDto : dto.items()) {
        if (itemDto.quantity() == null || itemDto.quantity() <= 0) {
            throw new BadRequestException("Cantidad inválida para productId=" + itemDto.productId());
        }

        Product p = productMap.get(itemDto.productId());
        if (p == null || !p.isActive()) {
            throw new ResourceNotFoundException("Producto no encontrado o inactivo (id=" + itemDto.productId() + ").");
        }

        if (buyerId != null && p.getSeller() != null && p.getSeller().getId() != null
                && buyerId.equals(p.getSeller().getId())) {
            throw new BadRequestException("No estás autorizado para comprar tu propio producto.");
        }

        int minQty = p.getMinPurchaseQuantity() == null ? 1 : p.getMinPurchaseQuantity();
        int maxQty = p.getMaxPurchaseQuantity() == null ? Integer.MAX_VALUE : p.getMaxPurchaseQuantity();
        if (itemDto.quantity() < minQty || itemDto.quantity() > maxQty) {
            throw new BadRequestException(String.format("La cantidad para el producto %d debe estar entre %d y %d.",
                    p.getId(), minQty, maxQty));
        }

        int available = digitalKeyRepository.countAvailableByProductId(p.getId());
        if (available < itemDto.quantity()) {
            throw new InsufficientStockException("Stock insuficiente para el producto: " + p.getTitle());
        }

        requiredPerProduct.merge(p.getId(), itemDto.quantity(), Integer::sum);

        BigDecimal unitPrice = p.getPrice() == null ? BigDecimal.ZERO : p.getPrice();
        subtotal = subtotal.add(unitPrice.multiply(BigDecimal.valueOf(itemDto.quantity())));
    }


    Order order = new Order();
    order.setBuyer(buyer);
    order.setStatus(OrderStatus.PENDING); //TODO: Fijarse si mantener en COMPLETED O PENDING al crear la order
    order.setNotes(dto.notes());
    order.setSubtotal(subtotal);

    List<OrderItem> orderItems = new ArrayList<>();
    for (OrderItemCreateDto itemDto : dto.items()) {
        Product prod = productMap.get(itemDto.productId());
        BigDecimal unitPrice = prod.getPrice() == null ? BigDecimal.ZERO : prod.getPrice();
        OrderItem oi = orderItemMapper.toEntityFromCreate(prod.getId(), itemDto.quantity(), unitPrice);
        oi.setOrder(order);
    
        orderItems.add(oi);
    }
    order.setItems(orderItems);


    BigDecimal totalDiscounts = BigDecimal.ZERO;
    Set<String> couponsUsedInThisOrder = new HashSet<>(); 

    for (int idx = 0; idx < dto.items().size(); idx++) {
        OrderItemCreateDto itemDto = dto.items().get(idx);
        OrderItem oi = orderItems.get(idx);

        Optional<Discount> bestAutoOpt = discountService.getHighestValueDiscountForOrderItem(itemDto);
        if (bestAutoOpt.isPresent()) {
            Discount best = bestAutoOpt.get();
            BigDecimal amount = discountService.calculateDiscountAmount(best, itemDto);
            oi.setDiscount(best);
            oi.setDiscountAmount(amount);
            totalDiscounts = totalDiscounts.add(amount != null ? amount : BigDecimal.ZERO);
          
            continue;
        }

      
        String itemCoupon = itemDto.couponCode();

        if (itemCoupon != null && !itemCoupon.isBlank()) {
            itemCoupon = itemCoupon.trim();
            if (couponsUsedInThisOrder.contains(itemCoupon)) {
           
                throw new BadRequestException("El cupón " + itemCoupon + " ya fue usado en otro ítem de esta orden.");
            }

         
            try {
                Optional<Discount> couponOpt = discountService.validateCouponCodeForOrderItem(itemCoupon, buyerId, itemDto, subtotal);
                if (couponOpt.isPresent()) {
                    Discount coupon = couponOpt.get();
                 
                    BigDecimal amount = discountService.calculateDiscountAmount(coupon, itemDto);
                    if (amount.compareTo(BigDecimal.ZERO) > 0) {
                        oi.setDiscount(coupon);
                        oi.setDiscountAmount(amount);
                        totalDiscounts = totalDiscounts.add(amount);
             
                        discountService.markCouponAsUsed(coupon.getId(), buyerId);
                        couponsUsedInThisOrder.add(itemCoupon);
                    }
                }
            } catch (ResourceNotFoundException | BadRequestException ex) {
        
                throw new BadRequestException("Cupón inválido para productId=" + itemDto.productId() + ": " + ex.getMessage());
            }
        } else {
            oi.setDiscountAmount(BigDecimal.ZERO);
        }
    }

   
    order.setDiscountAmount(totalDiscounts.setScale(2, RoundingMode.HALF_UP));
    order.setTotalAmount(order.getSubtotal().subtract(order.getDiscountAmount()).max(BigDecimal.ZERO));

   
    Order savedOrder = orderRepository.save(order);

    List<Long> sortedProductIds = requiredPerProduct.keySet().stream().sorted().collect(Collectors.toList());
    Instant now = Instant.now();

  
    Map<Long, List<DigitalKey>> reservedCandidates = new HashMap<>();


    for (Long pid : sortedProductIds) {
        int qtyNeeded = requiredPerProduct.get(pid);
        
        Page<DigitalKey> availablePage = digitalKeyRepository.findAvailableForUpdateByProductId(
                productMap.get(pid), KeyStatus.AVAILABLE, PageRequest.of(0, qtyNeeded)
        );
        List<DigitalKey> available = availablePage.getContent();

        if (available.size() < qtyNeeded) {
          
            throw new InsufficientStockException("Stock insuficiente durante la confirmación para el producto id=" + pid);
        }

      
        reservedCandidates.put(pid, available);
    }

   
    for (Long pid : sortedProductIds) {
        int qtyNeeded = requiredPerProduct.get(pid);

   
        List<OrderItem> itemsForProduct = savedOrder.getItems().stream()
                .filter(oi -> oi.getProduct() != null && Objects.equals(oi.getProduct().getId(), pid))
                .collect(Collectors.toList());

        int totalRequested = itemsForProduct.stream().mapToInt(OrderItem::getQuantity).sum();
        if (totalRequested != qtyNeeded) {
            throw new BadRequestException("Inconsistencia en cantidades solicitadas para productId=" + pid);
        }

 
        Deque<Long> keysQueue = reservedCandidates.get(pid).stream()
                .map(DigitalKey::getId)
                .collect(Collectors.toCollection(ArrayDeque::new));

        for (OrderItem oi : itemsForProduct) {
            int qtyForOi = oi.getQuantity();
            List<Long> keysForOi = new ArrayList<>(qtyForOi);
            for (int i = 0; i < qtyForOi; i++) {
                if (keysQueue.isEmpty()) {
                    throw new InsufficientStockException("No se pudieron asignar todas las claves necesarias para productId=" + pid);
                }
                keysForOi.add(keysQueue.poll());
            }

  
            int assigned = digitalKeyRepository.markAsSoldAndAssignToOrderItemBulk(
                    keysForOi,
                    KeyStatus.SOLD.name(),
                    KeyStatus.AVAILABLE.name(),
                    now,
                    oi.getId()
            );

            if (assigned < keysForOi.size()) {
                throw new InsufficientStockException("No se pudieron asignar todas las claves necesarias para productId=" + pid);
            }

            List<DigitalKey> soldKeys = digitalKeyRepository.findAllById(keysForOi);
            soldKeys.forEach(k -> {
                k.setStatus(KeyStatus.SOLD);
                k.setSoldAt(now);
                OrderItem link = new OrderItem();
                link.setId(oi.getId());
                k.setOrderItem(link);
            });
            oi.setDigitalKeys(new ArrayList<>(soldKeys));
        }
    }

    int updateOrderStatus = orderRepository.updateOrderStatus(savedOrder.getId(), OrderStatus.COMPLETED, now);
    if (updateOrderStatus == 0) {
        throw new BadRequestException("No se pudo marcar la orden como completada.");
    }

    Optional<Order> refreshed = orderRepository.findOrderWithItemsAndKeys(savedOrder.getId());
    if (refreshed.isPresent()) savedOrder = refreshed.get();



    BigDecimal buyerBalance = buyer.getBuyerBalance();
    buyerBalance = buyerBalance.add(savedOrder.getTotalAmount());

    while (buyerBalance.compareTo(BigDecimal.valueOf(100)) >= 0) {
        buyerBalance = buyerBalance.subtract(BigDecimal.valueOf(100));
        discountService.generateNewRandomCoupon(buyer.getId());
    }

    userService.updateBuyerBalance(buyer.getId(), buyerBalance);



    boolean includeKeyCodes = true;
    return orderMapper.toResponse(savedOrder, includeKeyCodes);
}
















    @Override
    public Page<OrderItemResponseDto> getOrderItemsByOrderId(Long orderId, Long requestingUserId)
            throws ResourceNotFoundException, UnauthorizedException {

        Order order = orderRepository.findOrderWithItemsAndProducts(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada (id=" + orderId + ")."));


        boolean includeKeyCodes = false;
        List<OrderItemResponseDto> dtos = order.getItems().stream()
                .map(oi -> orderItemMapper.toResponse(oi, includeKeyCodes))
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, Pageable.unpaged(), dtos.size());
    }

    @Override
    public Optional<OrderResponseDto> getOrderById(Long id, Long requestingUserId) throws ResourceNotFoundException, UnauthorizedException {
        Order order = orderRepository.findOrderWithItemsAndKeys(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada (id=" + id + ")."));


        boolean includeKeyCodes = false; 
        return Optional.of(orderMapper.toResponse(order, includeKeyCodes));
    }

    @Override
    public Page<OrderResponseDto> getOrdersByBuyer(Long buyerId, Pageable pageable) {
        Page<Order> page = orderRepository.findByBuyerId(buyerId, pageable);
        List<OrderResponseDto> dtos = page.getContent().stream()
                .map(o -> orderMapper.toResponse(o, false))
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Override
    public Page<OrderResponseDto> getOrdersBySeller(Long sellerId, Pageable pageable) {
        Page<Order> page = orderRepository.findBySellerId(sellerId, pageable);
        List<OrderResponseDto> dtos = page.getContent().stream()
                .map(o -> orderMapper.toResponse(o, false))
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Override
    public Page<OrderResponseDto> getAllOrders(Pageable pageable) {
        Page<Order> page = orderRepository.findAll(pageable);
        List<OrderResponseDto> dtos = page.getContent().stream()
                .map(o -> orderMapper.toResponse(o, false))
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Override
    @Transactional(rollbackOn = Throwable.class)
    public OrderResponseDto completeOrder(Long orderId, Long performedByUserId)
            throws ResourceNotFoundException, UnauthorizedException, BadRequestException {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada (id=" + orderId + ")."));

        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new BadRequestException("La orden ya está completada.");
        }

        Instant now = Instant.now();
        int updated = orderRepository.updateOrderStatus(orderId, OrderStatus.COMPLETED, now);
        if (updated == 0) {
            throw new BadRequestException("No se pudo marcar la orden como completada.");
        }

        Order completed = orderRepository.findOrderWithItemsAndKeys(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada luego de actualizar (id=" + orderId + ")."));

        boolean includeKeyCodes = false;
        return orderMapper.toResponse(completed, includeKeyCodes);
    }

    @Override
    @Transactional(rollbackOn = Throwable.class)
    public OrderResponseDto updateOrderStatus(Long orderId, String status, Long performedByUserId)
            throws ResourceNotFoundException, UnauthorizedException, BadRequestException {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada (id=" + orderId + ")."));

        OrderStatus newStatus;
        try {
            newStatus = OrderStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Estado inválido: " + status);
        }

        Instant completedAt = newStatus == OrderStatus.COMPLETED ? Instant.now() : null;
        int updated = orderRepository.updateOrderStatus(orderId, newStatus, completedAt);
        if (updated == 0) {
            throw new BadRequestException("No se pudo actualizar el estado de la orden.");
        }

        Order updatedOrder = orderRepository.findOrderWithItemsAndKeys(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada luego de actualizar (id=" + orderId + ")."));

        boolean includeKeyCodes = false;
        return orderMapper.toResponse(updatedOrder, includeKeyCodes);
    }
}
