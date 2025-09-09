package com.uade.tpo.marketplace.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.marketplace.entity.basic.Category;
import com.uade.tpo.marketplace.entity.basic.Discount;
import com.uade.tpo.marketplace.entity.basic.Product;
import com.uade.tpo.marketplace.entity.basic.User;
import com.uade.tpo.marketplace.entity.dto.create.DiscountCreateDto;
import com.uade.tpo.marketplace.entity.dto.create.OrderItemCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.DiscountResponseDto;
import com.uade.tpo.marketplace.entity.dto.update.DiscountUpdateDto;
import com.uade.tpo.marketplace.entity.enums.DiscountScope;
import com.uade.tpo.marketplace.entity.enums.DiscountType;
import com.uade.tpo.marketplace.entity.enums.Role;
import com.uade.tpo.marketplace.exceptions.BadRequestException;
import com.uade.tpo.marketplace.exceptions.CategoryNotFoundException;
import com.uade.tpo.marketplace.exceptions.CouponAlreadyUsedException;
import com.uade.tpo.marketplace.exceptions.CouponNotApplicableException;
import com.uade.tpo.marketplace.exceptions.CouponTargetBuyerMissingException;
import com.uade.tpo.marketplace.exceptions.DiscountNotFoundException;
import com.uade.tpo.marketplace.exceptions.DuplicateCouponException;
import com.uade.tpo.marketplace.exceptions.DuplicateResourceException;
import com.uade.tpo.marketplace.exceptions.OrderNotFoundException;
import com.uade.tpo.marketplace.exceptions.ProductNotFoundException;
import com.uade.tpo.marketplace.exceptions.ResourceNotFoundException;
import com.uade.tpo.marketplace.exceptions.UnauthorizedException;
import com.uade.tpo.marketplace.exceptions.UserNotFoundException;
import com.uade.tpo.marketplace.extra.mappers.DiscountMapper;
import com.uade.tpo.marketplace.repository.interfaces.ICategoryRepository;
import com.uade.tpo.marketplace.repository.interfaces.IDiscountRepository;
import com.uade.tpo.marketplace.repository.interfaces.IProductRepository;
import com.uade.tpo.marketplace.repository.interfaces.IUserRepository;
import com.uade.tpo.marketplace.service.interfaces.IDiscountService;



@Service
public class DiscountService implements IDiscountService {
    
    @Autowired
    private IDiscountRepository discountRepository;
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private ICategoryRepository categoryRepository;
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private DiscountMapper discountMapper;




    @Override
    @Transactional(rollbackFor = Throwable.class)
    public DiscountResponseDto createDiscount(DiscountCreateDto dto, Long createdByUserId)
            throws DuplicateResourceException, ResourceNotFoundException {


        if (dto == null) throw new BadRequestException("Datos de descuento no proporcionados.");


        if (dto.type() == null) {
            throw new BadRequestException("El tipo de descuento (PERCENT o FIXED) debe ser especificado.");
        }

        if (dto.scope() == null) {
            throw new BadRequestException("El ámbito del descuento (scope) debe ser especificado.");
        }

        if (dto.code() != null){
            if (discountRepository.existsByCode(dto.code())) {
                throw new DuplicateCouponException("Ya existe un cupón con el código: " + dto.code());
            }
        }

  
        if (dto.targetProductId() != null) {
            Product product = productRepository.findById(dto.targetProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto objetivo no encontrado (id=" + dto.targetProductId() + ")."));
            if (!product.getSeller().getId().equals(createdByUserId)) {
                throw new UnauthorizedException("El usuario solicitante no puede crear descuentos dirigidos a productos que no le pertenecen.");
            }
        }
        if (dto.targetCategoryId() != null) {
            categoryRepository.findById(dto.targetCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría objetivo no encontrada (id=" + dto.targetCategoryId() + ")."));
            Optional<User> user = userRepository.findById(createdByUserId);
            if (user.isEmpty() || !user.get().getRole().equals(Role.ADMIN)) {
                throw new UnauthorizedException("El usuario solicitante no puede crear descuentos dirigidos a categorías.");
            }

        }
        if (dto.targetSellerId() != null) {
            userRepository.findById(dto.targetSellerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Vendedor objetivo no encontrado (id=" + dto.targetSellerId() + ")."));
            if (!dto.targetSellerId().equals(createdByUserId)) {
                throw new UnauthorizedException("El usuario solicitante no puede crear descuentos dirigidos a otros vendedores.");
            }
        }
        if (dto.targetBuyerId() != null) {
            userRepository.findById(dto.targetBuyerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Comprador objetivo no encontrado (id=" + dto.targetBuyerId() + ")."));
            if (dto.targetBuyerId().equals(createdByUserId)) {
                throw new UnauthorizedException("El usuario solicitante no puede crear descuentos dirigidos a si mismo.");
            }
        }

        if (dto.targetProductId() == null && dto.targetCategoryId() == null && dto.targetSellerId() == null) {
            throw new BadRequestException("Debe especificarse al menos un objetivo para el descuento: targetProductId, targetCategoryId o targetSellerId.");
        }

        if (dto.type() == DiscountType.FIXED) {
            if (dto.targetBuyerId() == null) {
                throw new CouponTargetBuyerMissingException("Los cupones (tipo FIXED) deben estar dirigidos a un comprador específico (targetBuyerId).");
            }
            if (dto.code() == null || dto.code().isBlank()) {
                throw new BadRequestException("Los cupones (tipo FIXED) deben tener un código (code) definido.");
            }
    }

        Discount entity = discountMapper.toEntity(dto);
        Discount saved = discountRepository.save(entity);
        return discountMapper.toResponse(saved);
    }


    @Override
    @Transactional(rollbackFor = Throwable.class)
    public DiscountResponseDto updateDiscount(Long id, DiscountUpdateDto dto, Long requestingUserId)
            throws ResourceNotFoundException, UnauthorizedException, DuplicateResourceException {

        Discount existing = discountRepository.findById(id)
                .orElseThrow(() -> new DiscountNotFoundException("Descuento no encontrado (id=" + id + ")."));

        if (requestingUserId == null) throw new BadRequestException("Id de usuario no proporcionado.");

        if (existing.getTargetSeller() == null || existing.getTargetSeller().getId() == null || !existing.getTargetSeller().getId().equals(requestingUserId) ) {
                throw new UnauthorizedException("No tienes permiso para realizar esta acción.");
        }

        if (userRepository.findById(requestingUserId).isEmpty()) {
            throw new UserNotFoundException("Usuario solicitante no encontrado (id=" + requestingUserId + ").");
        }
        

        if (dto.code() != null && !dto.code().equalsIgnoreCase(existing.getCode())) {
            if (discountRepository.existsByCode(dto.code())) {
                throw new DuplicateCouponException("Ya existe un cupón con el código: " + dto.code());
            }
        }

    
        if (dto.targetProductId() != null) {
            Product product = productRepository.findById(dto.targetProductId())
                    .orElseThrow(() -> new ProductNotFoundException("Producto objetivo no encontrado (id=" + dto.targetProductId() + ")."));
            if (!product.getSeller().getId().equals(requestingUserId)) {
                throw new UnauthorizedException("El usuario solicitante no puede crear descuentos dirigidos a productos que no le pertenecen.");
            }
        }
        if (dto.targetCategoryId() != null) {
            categoryRepository.findById(dto.targetCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("Categoría objetivo no encontrada (id=" + dto.targetCategoryId() + ")."));
            Optional<User> user = userRepository.findById(requestingUserId);
            if (user.isEmpty() || !user.get().getRole().equals(Role.ADMIN)) {
                throw new UnauthorizedException("El usuario solicitante no puede crear descuentos dirigidos a categorías.");
            }

        }
        if (dto.targetSellerId() != null) {
            userRepository.findById(dto.targetSellerId())
                    .orElseThrow(() -> new UserNotFoundException("Vendedor objetivo no encontrado (id=" + dto.targetSellerId() + ")."));
            if (!dto.targetSellerId().equals(requestingUserId)) {
                throw new UnauthorizedException("El usuario solicitante no puede crear descuentos dirigidos a otros vendedores.");
            }
        }
        if (dto.targetBuyerId() != null) {
            userRepository.findById(dto.targetBuyerId())
                    .orElseThrow(() -> new UserNotFoundException("Comprador objetivo no encontrado (id=" + dto.targetBuyerId() + ")."));
            if (dto.targetBuyerId().equals(requestingUserId)) {
                throw new UnauthorizedException("El usuario solicitante no puede crear descuentos dirigidos a si mismo.");
            }
        }


        if (dto.type() != null && dto.type() == DiscountType.FIXED) {
            Long tb = dto.targetBuyerId() != null ? dto.targetBuyerId() :
                    (existing.getTargetBuyer() != null ? existing.getTargetBuyer().getId() : null);
            if (tb == null) {
                throw new CouponTargetBuyerMissingException("Los cupones (tipo FIXED) deben estar dirigidos a un comprador específico (targetBuyerId).");
            }
            if (dto.code() == null || dto.code().isBlank()) {
                throw new BadRequestException("Los cupones (tipo FIXED) deben tener un código (code) definido.");
            }
        }
        discountMapper.updateFromDto(dto, existing);
        Discount saved = discountRepository.save(existing);
        return discountMapper.toResponse(saved);
    }







    @Override
    public Optional<DiscountResponseDto> getDiscountById(Long id) {
        return discountRepository.findById(id).map(discountMapper::toResponse);
    }

    @Override
    public Optional<DiscountResponseDto> getActiveDiscountByCode(String code) {
        if (code == null) return Optional.empty();
  
        Optional<Discount> d = discountRepository.findByCodeAndActive(code);
        return d.filter(this::isActiveNow).map(discountMapper::toResponse);
    }






    @Override
    public BigDecimal calculateDiscountAmount(Discount discount, OrderItemCreateDto item) {
        if (discount == null) return BigDecimal.ZERO.setScale(2);
        if (!discount.isActive() || !isActiveNow(discount)) return BigDecimal.ZERO.setScale(2);
        if (item == null) return BigDecimal.ZERO.setScale(2);


        Product product = productRepository.findById(item.productId()).orElse(null);
        BigDecimal unitPrice = product != null && product.getPrice() != null ? product.getPrice() : BigDecimal.ZERO;
        int qty = item.quantity() == null ? 0 : item.quantity();
        BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(qty));


        if (discount.getMinQuantity() != null && qty < discount.getMinQuantity()) {
            return BigDecimal.ZERO.setScale(2);
        }


        boolean applies = false;
        DiscountScope scope = discount.getScope();
        if (scope == null) {
            throw new BadRequestException("El ámbito (scope) del descuento no está definido, por lo que no se puede calcular el descuento.");
        }

        switch (scope) {
            case PRODUCT -> {
                applies = discount.getTargetProduct() != null && product != null
                        && Objects.equals(discount.getTargetProduct().getId(), product.getId());
            }
            case CATEGORY -> {
                if (discount.getTargetCategory() != null && product != null && product.getCategories() != null) {
                    applies = product.getCategories().stream()
                            .anyMatch(c -> Objects.equals(c.getId(), discount.getTargetCategory().getId()));
                }
            }
            case SELLER -> {
                applies = discount.getTargetSeller() != null && product != null && product.getSeller() != null
                        && Objects.equals(product.getSeller().getId(), discount.getTargetSeller().getId());
            }
            default -> applies = false;
        }

        if (!applies) return BigDecimal.ZERO.setScale(2);

        BigDecimal amount = BigDecimal.ZERO;
        DiscountType type = discount.getType();

        if (type == DiscountType.PERCENT) {
            BigDecimal pct = discount.getValue() == null ? BigDecimal.ZERO : discount.getValue();
            amount = lineTotal.multiply(pct).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else if (type == DiscountType.FIXED) {
  
            BigDecimal fixed = discount.getValue() == null ? BigDecimal.ZERO : discount.getValue();
            amount = fixed;
        } else {
            amount = BigDecimal.ZERO;
        }

  
        if (amount.compareTo(lineTotal) > 0) amount = lineTotal;

        return amount.setScale(2, RoundingMode.HALF_UP);
    }













    @Override
    public Page<DiscountResponseDto> getActiveDiscounts(Pageable pageable, Optional<Boolean> onlyActive) {
        if (onlyActive.isPresent() && onlyActive.get()) {
            Page<Discount> page = discountRepository.findCurrentlyActive(pageable);
            List<DiscountResponseDto> dtos = page.getContent().stream().map(discountMapper::toResponse).collect(Collectors.toList());
            return new PageImpl<>(dtos, pageable, page.getTotalElements());
        } else {
            Page<Discount> page = discountRepository.findAll(pageable);
            List<DiscountResponseDto> dtos = page.getContent().stream().map(discountMapper::toResponse).collect(Collectors.toList());
            return new PageImpl<>(dtos, pageable, page.getTotalElements());
        }
    }

    @Override
    public Page<DiscountResponseDto> getActiveDiscountsForProduct(Long productId, Integer productQuantity, Pageable pageable) {
        if (productId == null) throw new BadRequestException("Producto no especificado.");
        Page<Discount> page = discountRepository.findActiveProductDiscounts(productId, pageable);
        List<Discount> filtered = page.getContent().stream()
                .filter(d -> d.getMinQuantity() == null || productQuantity == null || productQuantity >= d.getMinQuantity())
                .collect(Collectors.toList());
        List<DiscountResponseDto> dtos = filtered.stream().map(discountMapper::toResponse).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Override
    public Page<DiscountResponseDto> getAllDiscounts(Pageable pageable) {
        Page<Discount> page = discountRepository.findAll(pageable);
        List<DiscountResponseDto> dtos = page.getContent().stream().map(discountMapper::toResponse).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Override
    public Optional<Discount> validateCouponCodeForOrderItem(String code, Long buyerId, OrderItemCreateDto item, BigDecimal subtotal)
            throws ResourceNotFoundException, BadRequestException {

        if (code == null || code.isBlank()) throw new BadRequestException("Código de descuento no proporcionado.");
        if (item == null) throw new BadRequestException("OrderItem no proporcionado.");

        Discount discount = discountRepository.findByCodeAndActive(code)
                .orElseThrow(() -> new DiscountNotFoundException("Cupón no encontrado o no activo."));

        if (!isActiveNow(discount)) throw new CouponAlreadyUsedException("El cupón no está vigente.");

        if (discount.getType() != DiscountType.FIXED) {
            throw new BadRequestException("El código proporcionado no corresponde a un cupón válido (debe ser FIXED).");
        }


        if (discount.getTargetBuyer() != null && discount.getTargetBuyer().getId() != null) {
            if (buyerId == null || !Objects.equals(discount.getTargetBuyer().getId(), buyerId)) {
                throw new CouponNotApplicableException("El cupón no es aplicable a este comprador.");
            }
        }
        else if (discount.getTargetBuyer() == null || discount.getTargetBuyer().getId() == null) {
            throw new CouponTargetBuyerMissingException("El cupón no está dirigido a ningún comprador específico, por lo tanto no es aplicable.");
     
        }


        Product product = productRepository.findById(item.productId()).orElse(null);
        if (product == null) {
            throw new OrderNotFoundException("Producto del ítem no encontrado.");
        }

        boolean applicable = false;

        if (discount.getTargetProduct() != null && discount.getTargetProduct().getId() != null) {
            applicable = Objects.equals(discount.getTargetProduct().getId(), product.getId());
        }
        if (!applicable && discount.getTargetCategory() != null && discount.getTargetCategory().getId() != null) {
            if (product.getCategories() != null) {
                applicable = product.getCategories().stream()
                        .anyMatch(c -> Objects.equals(c.getId(), discount.getTargetCategory().getId()));
            }
        }
        if (!applicable && discount.getTargetSeller() != null && discount.getTargetSeller().getId() != null) {
            if (product.getSeller() != null) {
                applicable = Objects.equals(product.getSeller().getId(), discount.getTargetSeller().getId());
            }
        }

        if (!applicable) {
            throw new CouponNotApplicableException("El cupón no es aplicable a este ítem de orden.");
        }

  
        int qty = item.quantity() == null ? 0 : item.quantity();
        BigDecimal unitPrice = product.getPrice() != null ? product.getPrice() : BigDecimal.ZERO;
        BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(qty));

        if (discount.getMinQuantity() != null && qty < discount.getMinQuantity()) {
            throw new BadRequestException("El cupón requiere una cantidad mínima de " + discount.getMinQuantity() + " unidades.");
        }
        if (discount.getMinPrice() != null && lineTotal.compareTo(discount.getMinPrice()) < 0) {
            throw new BadRequestException("El cupón requiere un subtotal mínimo para aplicarse.");
        }
        if (discount.getMaxPrice() != null && lineTotal.compareTo(discount.getMaxPrice()) > 0) {
            throw new CouponNotApplicableException("El cupón no es aplicable para montos mayores a " + discount.getMaxPrice());
        }

        BigDecimal amount = calculateDiscountAmount(discount, item);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CouponNotApplicableException("El cupón no genera un descuento aplicable a este ítem.");
        }

        return Optional.of(discount);
    }


    @Override
    public Optional<Discount> getHighestValueDiscountForOrderItem(OrderItemCreateDto item) {

        if (item == null || item.productId() == null) return Optional.empty();

            Product product = productRepository.findById(item.productId()).orElse(null);
            if (product == null) return Optional.empty();

   
            Discount bestProduct = discountRepository.getHighestValueDiscountsForProduct(product.getId())
                    .stream().findFirst().orElse(null);

         
            Discount bestCategory = product.getCategories() == null ? null :
                    product.getCategories().stream()
                        .map(cat -> discountRepository.getHighestValueDiscountsForCategory(cat.getId())
                                .stream().findFirst().orElse(null))
                        .filter(Objects::nonNull)
                        .max(Comparator.comparing(Discount::getValue))
                        .orElse(null);


            Discount bestSeller = null;
            if (product.getSeller() != null && product.getSeller().getId() != null) {
                bestSeller = discountRepository.getHighestValueDiscountsForSeller(product.getSeller().getId())
                        .stream().findFirst().orElse(null);
            }

            List<Discount> candidates = new ArrayList<>();
            if (bestProduct != null) candidates.add(bestProduct);
            if (bestCategory != null) candidates.add(bestCategory);
            if (bestSeller != null) candidates.add(bestSeller);

            if (candidates.isEmpty()) return Optional.empty();

         
            candidates.sort(Comparator.comparing(Discount::getValue).reversed());
            return Optional.of(candidates.get(0));
    }


    @Override
    public Page<DiscountResponseDto> getAllActiveCouponsByTargetBuyerId(Long targetBuyerId, Pageable pageable) {
        if (targetBuyerId == null) throw new BadRequestException("Id de comprador no proporcionado.");
        Page<Discount> page = discountRepository.getAllActiveCouponsByTargetBuyerId(targetBuyerId, pageable);
        List<DiscountResponseDto> dtos = page.getContent().stream().map(discountMapper::toResponse).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }


    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void markCouponAsUsed(Long discountId, Long targetBuyerId) throws ResourceNotFoundException {
        if (discountId == null) throw new ResourceNotFoundException("Id de cupón no proporcionado.");
        if (targetBuyerId == null) throw new ResourceNotFoundException("Id de comprador no proporcionado.");

        Discount discount = discountRepository.findById(discountId)
            .orElseThrow(() -> new DiscountNotFoundException("Cupón no encontrado (id=" + discountId + ")."));

        if (!discount.isActive()) {
            throw new CouponAlreadyUsedException("El cupón no está activo, por lo tanto no se puede marcar como utilizado.");
        }


        if (discount.getTargetBuyer() == null || !Objects.equals(discount.getTargetBuyer().getId(), targetBuyerId)) {
            throw new UnauthorizedException("El cupón no es dirigido a este comprador, por lo tanto no se puede marcar como utilizado.");
        }

        int updated = discountRepository.markCouponAsUsed(discountId, targetBuyerId);
        if (updated == 0) {
   
            Discount d = discountRepository.findById(discountId).orElse(null);
            if (d == null) throw new DiscountNotFoundException("Cupón no encontrado (id=" + discountId + ").");
     
            d.setActive(false);
            discountRepository.save(d);
        }
    }




    private boolean isActiveNow(Discount d) {
        if (d == null) return false;
        if (!d.isActive()) return false;
        Instant now = Instant.now();
        if (d.getStartsAt() != null && now.isBefore(d.getStartsAt())) return false;
        if (d.getEndsAt() != null && now.isAfter(d.getEndsAt())) return false;
        if (d.getExpiresAt() != null && now.isAfter(d.getExpiresAt())) return false;
        return true;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Optional<DiscountResponseDto> generateNewRandomCoupon(Long targetBuyerId) {
        if (targetBuyerId == null) return Optional.empty();
        String code = generateCouponCode();
        Optional<Discount> d = discountRepository.findByCode(code);
        
        while (d.isPresent()) {
            code = generateCouponCode();
            d = discountRepository.findByCode(code);
        }
        DiscountType type = DiscountType.FIXED;
        if (Math.random() < 0.5) type = DiscountType.PERCENT;

        
        BigDecimal value = new BigDecimal(Math.random() * 100).setScale(2, RoundingMode.HALF_UP);

        DiscountScope scope = DiscountScope.PRODUCT;
        if (Math.random() < 0.5) scope = DiscountScope.CATEGORY;
        if (Math.random() < 0.5) scope = DiscountScope.SELLER;

        Long targetProductId = null;
        Long targetCategoryId = null;
        Long targetSellerId = null;

        if(scope == DiscountScope.SELLER) {
            targetSellerId = getRandomSellerId();
        }
        else if(scope == DiscountScope.CATEGORY) {
            targetCategoryId = getRandomCategoryId();
        }
        else {
            targetProductId = getRandomProductId();
        }

        Integer minQuantity = (int) Math.round(Math.random() * 100);
        Integer maxQuantity = minQuantity + (int) Math.round(Math.random() * 100);

        Instant startsAt = Instant.now().plusSeconds((long) Math.round(Math.random() * 100));
        Instant endsAt = startsAt.plusSeconds((long) Math.round(Math.random() * 100));

        BigDecimal minPrice = new BigDecimal(Math.random() * 100).setScale(2, RoundingMode.HALF_UP);
        BigDecimal maxPrice = minPrice.add(new BigDecimal(Math.random() * 100).setScale(2, RoundingMode.HALF_UP));


        Discount discount = new Discount();
        discount.setCode(code);
        discount.setType(type);
        discount.setValue(value);
        discount.setScope(scope);
        discount.setTargetBuyer(userRepository.findById(targetBuyerId).orElse(null));
        discount.setTargetSeller(scope == DiscountScope.SELLER ? userRepository.findById(targetSellerId).orElse(null) : null);
        discount.setTargetCategory(scope == DiscountScope.CATEGORY ? categoryRepository.findById(targetCategoryId).orElse(null) : null);
        discount.setTargetProduct(scope == DiscountScope.PRODUCT ? productRepository.findById(targetProductId).orElse(null) : null);
        discount.setMinQuantity(minQuantity);
        discount.setMaxQuantity(maxQuantity);
        discount.setStartsAt(startsAt);
        discount.setEndsAt(endsAt);
        discount.setExpiresAt(endsAt);
        discount.setMinPrice(minPrice);
        discount.setMaxPrice(maxPrice);
        discount.setActive(true);
        discount.setExpiresAt(endsAt);

        discountRepository.save(discount);
        return Optional.of(discountMapper.toResponse(discount));
    }

    private Long getRandomProductId() {
        List<Product> products = productRepository.findAll();
        if (!products.isEmpty()) {
            return products.get((int) Math.round(Math.random() * (products.size() - 1))).getId();
        }
        return null; 
    }

    private Long getRandomCategoryId() {
        List<Category> categories = categoryRepository.findAll();
        if (!categories.isEmpty()) {
            return categories.get((int) Math.round(Math.random() * (categories.size() - 1))).getId();
        }
        return null;
    }


    private Long getRandomSellerId() {
        List<User> users = userRepository.findAll();
        if (!users.isEmpty()) {
            List<User> sellers = users.stream()
                    .filter(user -> user.getRole().equals(Role.SELLER))
                    .collect(Collectors.toList());
            
            if (!sellers.isEmpty()) {
                return sellers.get((int) Math.round(Math.random() * (sellers.size() - 1))).getId();
            }
            else{
                throw new UserNotFoundException("No se encontraron vendedores.");
                
            }
        }
        return null;
        
    }
private String generateCouponCode() {
    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    Random random = new Random();

    StringBuilder couponCode = new StringBuilder();
    for (int i = 0; i < 4; i++) {
        if (i > 0) couponCode.append("-"); 
        for (int j = 0; j < 4; j++) {
            int randomIndex = random.nextInt(characters.length());
            couponCode.append(characters.charAt(randomIndex));
        }
    }
    return couponCode.toString();
}
}