package com.uade.tpo.marketplace.extra.mappers;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.uade.tpo.marketplace.entity.basic.OrderItem;
import com.uade.tpo.marketplace.entity.basic.Product;
import com.uade.tpo.marketplace.entity.dto.response.OrderItemDigitalKeyResponseDto;
import com.uade.tpo.marketplace.entity.dto.response.OrderItemResponseDto;

@Component
public class OrderItemMapper {

 
    public OrderItemResponseDto toResponse(OrderItem item, Boolean includeKeyCode){
        if (item == null) return null;

        Long productId = item.getProduct() != null ? item.getProduct().getId() : null;
        String productTitle = item.getProduct() != null ? item.getProduct().getTitle() : null;

        List<OrderItemDigitalKeyResponseDto> keys = Optional.ofNullable(item.getDigitalKeys())
            .orElse(Collections.emptyList())
            .stream()
            .filter(Objects::nonNull)
            .map(dk -> new OrderItemDigitalKeyResponseDto(
                    includeKeyCode ? dk.getKeyCode() : null,
                    dk.getKeyMask()
            ))
            .collect(Collectors.toList());

        return new OrderItemResponseDto(
            item.getId(),
            productId,
            productTitle,
            item.getUnitPrice(),
            item.getQuantity(),
            item.getLineTotal(),
            keys
        );
    }

 
    public OrderItem toEntityFromCreate(Long productId, int quantity, java.math.BigDecimal unitPrice){
        OrderItem oi = new OrderItem();
        Product p = new Product();
        p.setId(productId);
        oi.setProduct(p);
        oi.setQuantity(quantity);
        oi.setUnitPrice(unitPrice);
        oi.setLineTotal(unitPrice.multiply(java.math.BigDecimal.valueOf(quantity)));
        return oi;
    }
}