package com.uade.tpo.marketplace.extra.mappers;

import org.springframework.stereotype.Component;

import com.uade.tpo.marketplace.entity.basic.DigitalKey;
import com.uade.tpo.marketplace.entity.basic.OrderItem;
import com.uade.tpo.marketplace.entity.basic.Product;
import com.uade.tpo.marketplace.entity.dto.response.OrderItemResponseDto;

@Component
public class OrderItemMapper {

 
    public OrderItemResponseDto toResponse(OrderItem item, boolean includeKeyCode){
        if (item == null) return null;
        Long productId = item.getProduct() != null ? item.getProduct().getId() : null;
        String productTitle = item.getProduct() != null ? item.getProduct().getTitle() : null;
        String keyCode = null;
        String keyMask = null;
        DigitalKey dk = item.getDigitalKey();
        if (dk != null) {
            keyMask = dk.getKeyMask();
            if (includeKeyCode) keyCode = dk.getKeyCode();
        }
        return new OrderItemResponseDto(
            item.getId(), productId, productTitle, item.getUnitPrice(),
            item.getQuantity(), item.getLineTotal(), keyCode, keyMask
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