package com.uade.tpo.marketplace.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entity.basic.DigitalKey;
import com.uade.tpo.marketplace.entity.basic.Discount;
import com.uade.tpo.marketplace.entity.basic.Order;
import com.uade.tpo.marketplace.entity.basic.OrderItem;
import com.uade.tpo.marketplace.entity.basic.Product;
import com.uade.tpo.marketplace.entity.basic.User;
import com.uade.tpo.marketplace.entity.dto.OrderCreateDto;
import com.uade.tpo.marketplace.entity.dto.OrderDto;
import com.uade.tpo.marketplace.entity.dto.OrderItemCreateDto;
import com.uade.tpo.marketplace.entity.dto.OrderItemDto;
import com.uade.tpo.marketplace.entity.enums.KeyStatus;
import com.uade.tpo.marketplace.entity.enums.OrderStatus;
import com.uade.tpo.marketplace.exceptions.BadRequestException;
import com.uade.tpo.marketplace.exceptions.OutOfStockException;
import com.uade.tpo.marketplace.exceptions.ProductNotFoundException;
import com.uade.tpo.marketplace.exceptions.ResourceNotFoundException;
import com.uade.tpo.marketplace.repository.interfaces.IDigitalKeyRepository;
import com.uade.tpo.marketplace.repository.interfaces.IOrderItemRepository;
import com.uade.tpo.marketplace.repository.interfaces.IOrderRepository;
import com.uade.tpo.marketplace.repository.interfaces.IProductRepository;
import com.uade.tpo.marketplace.repository.interfaces.IUserRepository;

@Service
public class OrderService {
    
    // private IOrderRepository orderRepository;
    // private IOrderItemRepository orderItemRepository;
    // private IProductRepository productRepository;
    // private IDigitalKeyRepository digitalKeyRepository;
    // private DiscountService discountService;
    // private IUserRepository userRepository;

    // public OrderService (IDigitalKeyRepository digitalKeyRepository, IOrderRepository orderRepository, IOrderItemRepository orderItemRepository, IProductRepository productRepository, DiscountService discountService, IUserRepository userRepository) {
    //     this.digitalKeyRepository = digitalKeyRepository;
    //     this.orderRepository = orderRepository;
    //     this.orderItemRepository = orderItemRepository;
    //     this.productRepository = productRepository;
    //     this.discountService = discountService;
    //     this.userRepository = userRepository;
    //     //AJUSTAR SEGÚN LAS CLASES CONCRETAS DE REPOSITORIOS  
    // }


    // //TODO: Revisar excepciones para hacerlas más específicas

    // public OrderDto createOrder(OrderCreateDto dto, Long buyerId) throws BadRequestException, OutOfStockException, ProductNotFoundException {
    //     if (dto.items() == null || dto.items().isEmpty()) {
    //         throw new BadRequestException("La orden de compra no puede estar vacía.");
    //     }

    
    //     User buyer = userRepository.getUserById(buyerId)
    //             .orElseThrow(() -> new ResourceNotFoundException("El comprador no fue encontrado: " + buyerId));


    //     Order order = new Order();
    //     order.setBuyerId(buyerId);
    //     order.setStatus(OrderStatus.PENDING);
    //     order.setCreatedAt(Instant.now());
    //     order.setItems(new ArrayList<>());

    //     BigDecimal subtotal = BigDecimal.ZERO;
    //     Map<Long, List<DigitalKey>> assignedKeysByProduct = new HashMap<>();

      
    //     List<Long> productIdsToLock = dto.items().stream().map(OrderItemCreateDto::productId).distinct().sorted().collect(Collectors.toList());
    //     List<ReentrantLock> acquiredLocks = new ArrayList<>();

    //     try {
    

   
    //         for (OrderItemCreateDto itemReq : dto.items()) {
    //             Long pid = itemReq.productId();
    //             Integer qty = itemReq.quantity();

    //             Product p = productRepository.getProductById(pid)
    //                     .orElseThrow(() -> new ProductNotFoundException());

    //             if (!p.isActive()) throw new BadRequestException("El producto no está activo: " + pid);

       
    //             if (Objects.equals(p.getSellerId(), buyerId)) {
    //                 throw new BadRequestException("No se puede comprar tu propio producto: " + pid);
    //             }

          
    //             List<DigitalKey> available = digitalKeyRepository.getAvailableKeysForProduct(pid, qty);
    //             if (available.size() < qty) {
    //                 throw new OutOfStockException();
    //             }

              
    //             BigDecimal unitPrice = p.getPrice();
    //             BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(qty));
    //             subtotal = subtotal.add(lineTotal);

    
    //             OrderItem item = OrderItem.builder()
    //                     .productId(pid)
    //                     .quantity(qty)
    //                     .unitPrice(unitPrice)
    //                     .lineTotal(lineTotal)
    //                     .build();

        
    //             order.getItems().add(item);

          
    //             assignedKeysByProduct.put(pid, available);
    //         }

      
    //         BigDecimal discountAmount = BigDecimal.ZERO;
    //         if (dto.couponCode() != null && !dto.couponCode().isBlank()) {
    //             Optional<Discount> opt = discountService.getValidDiscountByCode(dto.couponCode());
    //             if (opt.isPresent()) {
    //                 discountAmount = discountService.calculateOrderLevelDiscountAmount(opt.get(), subtotal);
    //             }
    //         }

    //         BigDecimal tax = BigDecimal.ZERO; 
    //         BigDecimal total = subtotal.subtract(discountAmount).add(tax);


    //         //order.setSubtotal(subtotal);
    //         //TODO: Revisar subtotal y total en Order
            
    //         order.setDiscountAmount(discountAmount);
    //         order.setTaxAmount(tax);
    //         order.setTotalAmount(total);
    //         order.setStatus(OrderStatus.COMPLETED);
    //         order.setCompletedAt(Instant.now());

    //         Order savedOrder = orderRepository.creatOrder(order);

    //         for (OrderItem item : savedOrder.getItems()) {
    //             Long pid = item.getProductId();
    //             List<DigitalKey> keys = assignedKeysByProduct.get(pid);

    //             List<OrderItem> persistedItems = new ArrayList<>();
    //             Integer idx = 0;
    //             for (Integer i = 0; i < item.getQuantity(); i++) {
    //                 DigitalKey assignedKey = keys.get(i);
    //                 OrderItem unitItem = OrderItem.builder()
    //                         .orderId(savedOrder.getId())
    //                         .productId(pid)
    //                         .digitalKeyId(assignedKey.getId())
    //                         .quantity(1)
    //                         .unitPrice(item.getUnitPrice())
    //                         .lineTotal(item.getUnitPrice())
    //                         .createdAt(Instant.now())
    //                         .build();
    //                 OrderItem savedItem = orderItemRepository.creaOrderItem(unitItem);
              
    //                 assignedKey.setStatus(KeyStatus.SOLD);
    //                 assignedKey.setSoldAt(Instant.now());
    //                 assignedKey.setOrderItemId(savedItem.getId());
    //                 digitalKeyRepository.save(assignedKey);
    //                 persistedItems.add(savedItem);
    //                 idx++;
    //             }
    //         }

      
          

    //         List<OrderItemDto> itemDtos = new ArrayList<>();
    //         List<OrderItem> finalItems = orderItemRepository.getOrderItemsByOrderId(savedOrder.getId());
    //         for (OrderItem oi : finalItems) {
    //             Product prod = productRepository.getProductById(oi.getProductId()).orElse(null);
    //             DigitalKey key = oi.getDigitalKeyId() == null ? null : digitalKeyRepository.getKeyById(oi.getDigitalKeyId()).orElse(null);
    //             String keyCodeForBuyer = key == null ? null : key.getKeyCode(); 
    //             itemDtos.add(new OrderItemDto(oi.getProductId(), prod != null ? prod.getTitle() : "", oi.getUnitPrice(), oi.getQuantity(), keyCodeForBuyer));
    //         }

    //         OrderDto response = new OrderDto(
    //                 savedOrder.getId(),
    //                 savedOrder.getBuyerId(),
    //                 savedOrder.getTotalAmount(),
    //                 savedOrder.getDiscountAmount(),
    //                 savedOrder.getTaxAmount(),
    //                 savedOrder.getStatus().name(),
    //                 itemDtos,
    //                 savedOrder.getCreatedAt(),
    //                 savedOrder.getCompletedAt()
    //         );



    //         return response;

    //     } finally {
     
    //         for (ReentrantLock l : acquiredLocks) {
    //             if (l.isHeldByCurrentThread()) l.unlock();
    //         }
    //     }
    // }

    // public OrderDto getOrderById(Long orderId, Long requestingUserId) {
    //     Order o = orderRepository.getOrderById(orderId).orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada: " + orderId));
   
    //     List<OrderItem> items = orderItemRepository.getOrderItemsByOrderId(o.getId());
    //     List<OrderItemDto> itemDtos = items.stream().map(oi -> {
    //         String keyCode = null;
    //         if (oi.getDigitalKeyId() != null) {
    //             keyCode = digitalKeyRepository.getKeyById(oi.getDigitalKeyId()).map(DigitalKey::getKeyCode).orElse(null);
    //         }
    //         Product prod = productRepository.getProductById(oi.getProductId()).orElse(null);
    //         return new OrderItemDto(oi.getProductId(), prod != null ? prod.getTitle() : "", oi.getUnitPrice(), oi.getQuantity(), keyCode);
    //     }).collect(Collectors.toList());

    //     return new OrderDto(o.getId(), o.getBuyerId(), o.getTotalAmount(), o.getDiscountAmount(), o.getTaxAmount(), o.getStatus().name(), itemDtos, o.getCreatedAt(), o.getCompletedAt());
    // }

    // public List<OrderDto> listOrdersForBuyer(Long buyerId) {
    //     return orderRepository.getOrdersByBuyerId(buyerId).stream().map(o -> getOrderById(o.getId(), buyerId)).collect(Collectors.toList());
    // }

}
