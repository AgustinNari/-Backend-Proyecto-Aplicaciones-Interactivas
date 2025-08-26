package com.uade.tpo.marketplace.extra.mappers;

import org.springframework.stereotype.Component;

import com.uade.tpo.marketplace.entity.basic.Category;
import com.uade.tpo.marketplace.entity.basic.Discount;
import com.uade.tpo.marketplace.entity.basic.Product;
import com.uade.tpo.marketplace.entity.basic.User;
import com.uade.tpo.marketplace.entity.dto.create.DiscountCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.DiscountResponseDto;
import com.uade.tpo.marketplace.entity.dto.update.DiscountUpdateDto;
import com.uade.tpo.marketplace.entity.enums.DiscountScope;
import com.uade.tpo.marketplace.entity.enums.DiscountType;

@Component
public class DiscountMapper {

    public Discount toEntity(DiscountCreateDto dto){
        if (dto == null) return null;
        Discount d = new Discount();
        d.setCode(dto.code());
        if (dto.type() != null) {
            try { d.setType(DiscountType.valueOf(dto.type())); } catch (Exception ignored) {}
        }
        d.setValue(dto.value());
        if (dto.scope() != null) {
            try { d.setScope(DiscountScope.valueOf(dto.scope())); } catch (Exception ignored) {}
        }
        if (dto.targetProductId() != null) {
            Product p = new Product(); p.setId(dto.targetProductId()); d.setTargetProduct(p);
        }
        if (dto.targetCategoryId() != null) {
            Category c = new Category(); c.setId(dto.targetCategoryId()); d.setTargetCategory(c);
        }
        if (dto.targetSellerId() != null) {
            User u = new User(); u.setId(dto.targetSellerId()); d.setTargetSeller(u);
        }
        d.setMinQuantity(dto.minQuantity());
        d.setStartsAt(dto.startsAt());
        d.setEndsAt(dto.endsAt());
        d.setMaxUses(dto.maxUses());
        d.setPerUserLimit(dto.perUserLimit());
        if (dto.active() != null) d.setActive(dto.active());
        return d;
    }

    public void updateFromDto(DiscountUpdateDto dto, Discount entity){
        if (dto == null || entity == null) return;
        if (dto.code() != null) entity.setCode(dto.code());
        if (dto.type() != null) {
            try { entity.setType(DiscountType.valueOf(dto.type())); } catch (Exception ignored) {}
        }
        if (dto.value() != null) entity.setValue(dto.value());
        if (dto.scope() != null) {
            try { entity.setScope(DiscountScope.valueOf(dto.scope())); } catch (Exception ignored) {}
        }
        if (dto.targetProductId() != null) { Product p = new Product(); p.setId(dto.targetProductId()); entity.setTargetProduct(p); }
        if (dto.targetCategoryId() != null) { Category c = new Category(); c.setId(dto.targetCategoryId()); entity.setTargetCategory(c); }
        if (dto.targetSellerId() != null) { User u = new User(); u.setId(dto.targetSellerId()); entity.setTargetSeller(u); }
        if (dto.minQuantity() != null) entity.setMinQuantity(dto.minQuantity());
        if (dto.startsAt() != null) entity.setStartsAt(dto.startsAt());
        if (dto.endsAt() != null) entity.setEndsAt(dto.endsAt());
        if (dto.maxUses() != null) entity.setMaxUses(dto.maxUses());
        if (dto.perUserLimit() != null) entity.setPerUserLimit(dto.perUserLimit());
        if (dto.active() != null) entity.setActive(dto.active());
    }

    public DiscountResponseDto toResponse(Discount d){
        if (d == null) return null;
        Long targetProductId = d.getTargetProduct() != null ? d.getTargetProduct().getId() : null;
        Long targetCategoryId = d.getTargetCategory() != null ? d.getTargetCategory().getId() : null;
        Long targetSellerId = d.getTargetSeller() != null ? d.getTargetSeller().getId() : null;
        String type = d.getType() == null ? null : d.getType().name();
        String scope = d.getScope() == null ? null : d.getScope().name();
        return new DiscountResponseDto(d.getId(), d.getCode(), type, d.getValue(), scope,
                targetProductId, targetCategoryId, targetSellerId,
                d.getMinQuantity(), d.getStartsAt(), d.getEndsAt(),
                d.getMaxUses(), d.getPerUserLimit(), d.isActive(), d.getCreatedAt());
    }
}