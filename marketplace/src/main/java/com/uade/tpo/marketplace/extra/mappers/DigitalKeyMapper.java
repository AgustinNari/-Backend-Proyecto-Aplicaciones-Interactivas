package com.uade.tpo.marketplace.extra.mappers;

import org.springframework.stereotype.Component;

import com.uade.tpo.marketplace.entity.basic.DigitalKey;
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
        k.setKeyCode(dto.keyCode());
        k.setKeyMask(dto.keyMask());
        return k;
    }

    public void updateFromDto(DigitalKeyUpdateDto dto, DigitalKey entity){
        if (dto == null || entity == null) return;
        if (dto.keyMask() != null) entity.setKeyMask(dto.keyMask());
        if (dto.status() != null) {
            try {
                entity.setStatus(Enum.valueOf(KeyStatus.class, dto.status()));
            } catch (Exception ignored) {}
        }
    }

    public DigitalKeyResponseDto toResponse(DigitalKey key){
        if (key == null) return null;
        Long productId = key.getProduct() != null ? key.getProduct().getId() : null;
        String status = key.getStatus() == null ? null : key.getStatus().name();
        return new DigitalKeyResponseDto(key.getId(), productId, key.getKeyMask(), status, key.getSoldAt(), key.getCreatedAt(), key.getKeyCode());
    }
}