package com.uade.tpo.marketplace.extra.mappers;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Component;

import com.uade.tpo.marketplace.entity.basic.Discount;
import com.uade.tpo.marketplace.entity.basic.OrderItem;
import com.uade.tpo.marketplace.entity.basic.Product;
import com.uade.tpo.marketplace.entity.dto.create.OrderItemCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.OrderItemDigitalKeyResponseDto;
import com.uade.tpo.marketplace.entity.dto.response.OrderItemResponseDto;
import com.uade.tpo.marketplace.entity.dto.update.OrderItemUpdateDto;
import com.uade.tpo.marketplace.repository.interfaces.IProductRepository;

@Component
public class OrderItemMapper {


    private final DigitalKeyMapper digitalKeyMapper;

    @Autowired
    private IProductRepository productRepository;

    public OrderItemMapper() {
        this.digitalKeyMapper = new DigitalKeyMapper();

    }

 
  public OrderItemResponseDto toResponse(OrderItem item, Boolean includeKeyCode){
        if (item == null) return null;

        Long productId = item.getProduct() != null ? safeGetId(item.getProduct()) : null;
        String productTitle = item.getProduct() != null ? safeGetTitle(item.getProduct()) : null;

        BigDecimal unitPrice = item.getUnitPrice() != null ? item.getUnitPrice() : BigDecimal.ZERO;
        Integer quantity = item.getQuantity() != null ? item.getQuantity() : 0;
        BigDecimal lineSubtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        BigDecimal discountAmount = item.getDiscountAmount() != null ? item.getDiscountAmount() : BigDecimal.ZERO;
        BigDecimal lineTotal = item.getLineTotal() != null ? item.getLineTotal() : lineSubtotal.subtract(discountAmount);

     
        List<OrderItemDigitalKeyResponseDto> keys = Optional.ofNullable(item.getDigitalKeys())
            .orElse(Collections.emptySet()) 
            .stream()
            .filter(Objects::nonNull)
            .map(dk -> {
                String mask = dk.getKeyMask();
                String code = includeKeyCode ? dk.getKeyCode() : null;
                return new OrderItemDigitalKeyResponseDto(code, mask);
            })
            .collect(Collectors.toList()); 

        return new OrderItemResponseDto(
            item.getId(),
            productId,
            productTitle,
            unitPrice,
            quantity,
            lineSubtotal,
            discountAmount,
            lineTotal,
            keys
        );
    }



    public List<OrderItemResponseDto> toResponseList(Collection<OrderItem> items, boolean includeKeyCode) {
        if (items == null || items.isEmpty()) return Collections.emptyList();
        return items.stream()
                .filter(Objects::nonNull)
                .map(i -> toResponse(i, includeKeyCode))
                .collect(Collectors.toList());
    }


 
    public OrderItem toEntityFromCreate(Long productId, int quantity, BigDecimal unitPrice){
        OrderItem oi = new OrderItem();
        Product p = productRepository.findById(productId).orElse(null);
        oi.setProduct(p);
        oi.setQuantity(quantity);
        oi.setUnitPrice(unitPrice != null ? unitPrice : BigDecimal.ZERO);
        BigDecimal lineSubtotal = oi.getUnitPrice().multiply(BigDecimal.valueOf(oi.getQuantity()));
        oi.setLineTotal(lineSubtotal);
        oi.setDiscountAmount(BigDecimal.ZERO);
        return oi;
    }

    
    public OrderItem toEntityFromCreate(OrderItemCreateDto dto, BigDecimal unitPrice){
        if (dto == null) return null;
        return toEntityFromCreate(dto.productId(), dto.quantity(), unitPrice);
    }


    public void updateFromDto(OrderItemUpdateDto dto, OrderItem entity) {
            if (dto == null || entity == null) return;
            if (dto.quantity() != null) {
                entity.setQuantity(dto.quantity());
                BigDecimal unit = entity.getUnitPrice() != null ? entity.getUnitPrice() : BigDecimal.ZERO;
                BigDecimal subtotal = unit.multiply(BigDecimal.valueOf(entity.getQuantity()));
                entity.setLineTotal(subtotal.subtract(entity.getDiscountAmount() != null ? entity.getDiscountAmount() : BigDecimal.ZERO));
            }
            if (dto.unitPrice() != null) {
                entity.setUnitPrice(dto.unitPrice());
                BigDecimal subtotal = dto.unitPrice().multiply(BigDecimal.valueOf(entity.getQuantity() != null ? entity.getQuantity() : 0));
                entity.setLineTotal(subtotal.subtract(entity.getDiscountAmount() != null ? entity.getDiscountAmount() : BigDecimal.ZERO));
            }
            if (dto.discountId() != null) {
                Discount discount = new Discount();
                discount.setId(dto.discountId());
                entity.setDiscount(discount);
            }
        }

    private Long safeGetId(Product p) {
        try { return p.getId(); } catch (Exception e) { return null; }
    }

    private String safeGetTitle(Product p) {
        try { return p.getTitle(); } catch (Exception e) { return null; }
    }

    }


