package com.uade.tpo.marketplace.extra.mappers;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.uade.tpo.marketplace.entity.basic.DigitalKey;
import com.uade.tpo.marketplace.entity.basic.OrderItem;
import com.uade.tpo.marketplace.entity.basic.Product;
import com.uade.tpo.marketplace.entity.dto.create.DigitalKeyCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.DigitalKeyResponseDto;
import com.uade.tpo.marketplace.entity.dto.update.DigitalKeyUpdateDto;
import com.uade.tpo.marketplace.entity.enums.KeyStatus;

@Component
public class DigitalKeyMapper {

    public DigitalKey toEntity(DigitalKeyCreateDto dto){
        if (dto == null) return null;
        DigitalKey k = new DigitalKey();
        if (dto.productId() != null) {
            Product p = new Product();
            p.setId(dto.productId());
            k.setProduct(p);
        }
        k.setKeyCode(dto.keyCode().trim());
        if (dto.keyMask() != null && StringUtils.hasText(dto.keyMask())) {
            k.setKeyMask(dto.keyMask().trim());
        } else {
            k.setKeyMask(maskKey(dto.keyCode()));
        }
        return k;
    }

    public List<DigitalKey> toEntitiesFromKeyCodes(Long productId, Collection<String> keyCodes) {
        if (keyCodes == null || keyCodes.isEmpty()) return Collections.emptyList();
        return keyCodes.stream()
                .filter(Objects::nonNull)
                .map(code -> {
                    String trimmed = code.trim();
                    DigitalKey k = new DigitalKey();
                    if (productId != null) {
                        Product p = new Product();
                        p.setId(productId);
                        k.setProduct(p);
                    }
                    k.setKeyCode(trimmed);
                    k.setKeyMask(maskKey(trimmed));
                    return k;
                }).collect(Collectors.toList());
    }

    private String maskKey(String key) {
        if (!StringUtils.hasText(key)) return null;
        String clean = key.trim();
        if (clean.contains("-")) {
            String[] parts = clean.split("-");
            for (int i = 0; i < parts.length - 1; i++) parts[i] = "****";
            return String.join("-", parts);
        }
        int len = clean.length();
        if (len <= 4) return "****" + clean;
        return "****" + clean.substring(len - 4);
    }

    public void updateFromDto(DigitalKeyUpdateDto dto, DigitalKey entity){
        if (dto == null || entity == null) return;
        if (dto.keyMask() != null) {
            entity.setKeyMask(dto.keyMask().trim());
        }
        if (dto.status() != null) {
            entity.setStatus(dto.status());
        }
    }

    public DigitalKeyResponseDto toResponse(DigitalKey key, boolean includeKeyCode){
        if (key == null) return null;
        Long id = key.getId();
        Long productId = key.getProduct() != null ? safeGetId(key.getProduct()) : null;
        Long orderItemId = key.getOrderItem() != null ? safeGetId(key.getOrderItem()) : null;
        String keyMask = key.getKeyMask();
        KeyStatus status = key.getStatus();
        Instant soldAt = key.getSoldAt();
        Instant createdAt = key.getCreatedAt();
        String keyCode = includeKeyCode ? key.getKeyCode() : null;
        return new DigitalKeyResponseDto(id, productId, keyMask, status, soldAt, createdAt, keyCode, orderItemId);
    }

    public List<DigitalKeyResponseDto> toResponseList(Collection<DigitalKey> keys, boolean includeKeyCode) {
        if (keys == null || keys.isEmpty()) return Collections.emptyList();
        return keys.stream()
                .filter(Objects::nonNull)
                .map(k -> toResponse(k, includeKeyCode))
                .collect(Collectors.toList());
    }


    

    private Long safeGetId(Product p) {
        try { return p.getId(); } catch (Exception e) { return null; }
    }

    private Long safeGetId(OrderItem oi) {
        try { return oi.getId(); } catch (Exception e) { return null; }
    }
}