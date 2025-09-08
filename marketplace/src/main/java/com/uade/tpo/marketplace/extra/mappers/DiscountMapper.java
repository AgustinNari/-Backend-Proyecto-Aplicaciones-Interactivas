package com.uade.tpo.marketplace.extra.mappers;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        if (dto.code() != null) d.setCode(dto.code().trim());

        if (dto.type() != null) d.setType(dto.type());
        if (dto.value() != null) d.setValue(dto.value());
        if (dto.scope() != null) d.setScope(dto.scope());

        if (dto.targetProductId() != null) d.setTargetProduct(fromProductId(dto.targetProductId()));
        if (dto.targetCategoryId() != null) d.setTargetCategory(fromCategoryId(dto.targetCategoryId()));
        if (dto.targetSellerId() != null) d.setTargetSeller(fromUserId(dto.targetSellerId()));
        if (dto.targetBuyerId() != null) d.setTargetBuyer(fromUserId(dto.targetBuyerId()));

        d.setMinQuantity(dto.minQuantity());
        d.setMaxQuantity(dto.maxQuantity());
        d.setStartsAt(dto.startsAt());
        d.setEndsAt(dto.endsAt());
        d.setMinPrice(dto.minPrice());
        d.setMaxPrice(dto.maxPrice());
        if (dto.expiresAt() != null) d.setExpiresAt(dto.expiresAt());
        return d;
    }

    public void updateFromDto(DiscountUpdateDto dto, Discount entity){
        if (dto == null || entity == null) return;
        if (dto.code() != null) entity.setCode(dto.code().trim());
        if (dto.type() != null) entity.setType(dto.type());
        if (dto.value() != null) entity.setValue(dto.value());
        if (dto.scope() != null) entity.setScope(dto.scope());

        if (dto.targetProductId() != null) entity.setTargetProduct(fromProductId(dto.targetProductId()));
        if (dto.targetCategoryId() != null) entity.setTargetCategory(fromCategoryId(dto.targetCategoryId()));
        if (dto.targetSellerId() != null) entity.setTargetSeller(fromUserId(dto.targetSellerId()));
        if (dto.targetBuyerId() != null) entity.setTargetBuyer(fromUserId(dto.targetBuyerId()));

        if (dto.minQuantity() != null) entity.setMinQuantity(dto.minQuantity());
        if (dto.maxQuantity() != null) entity.setMaxQuantity(dto.maxQuantity());
        if (dto.startsAt() != null) entity.setStartsAt(dto.startsAt());
        if (dto.endsAt() != null) entity.setEndsAt(dto.endsAt());
        if (dto.minPrice() != null) entity.setMinPrice(dto.minPrice());
        if (dto.maxPrice() != null) entity.setMaxPrice(dto.maxPrice());
        if (dto.active() != null) entity.setActive(dto.active());
        if (dto.expiresAt() != null) entity.setExpiresAt(dto.expiresAt());
    }

    public DiscountResponseDto toResponse(Discount d){
        if (d == null) return null;

        Long targetProductId = d.getTargetProduct() != null ? safeGetId(d.getTargetProduct()) : null;
        Long targetCategoryId = d.getTargetCategory() != null ? safeGetId(d.getTargetCategory()) : null;
        Long targetSellerId = d.getTargetSeller() != null ? safeGetId(d.getTargetSeller()) : null;
        Long targetBuyerId = d.getTargetBuyer() != null ? safeGetId(d.getTargetBuyer()) : null;

        DiscountType type = d.getType();
        DiscountScope scope = d.getScope();

        return new DiscountResponseDto(
            d.getId(),
            d.getCode(),
            type,
            d.getValue(),
            scope,
            targetProductId,
            targetCategoryId,
            targetSellerId,
            d.getMinQuantity(),
            d.getMaxQuantity(),
            d.getStartsAt(),
            d.getEndsAt(),
            d.getMinPrice(),
            d.getMaxPrice(),
            d.isActive(),
            d.getCreatedAt(),
            d.getExpiresAt(),
            targetBuyerId
        );
    }


     public List<DiscountResponseDto> toResponseList(Collection<Discount> discounts) {
        if (discounts == null || discounts.isEmpty()) return Collections.emptyList();
        return discounts.stream()
                .filter(Objects::nonNull)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }



    public Product fromProductId(Long id) {
        if (id == null) return null;
        Product p = new Product();
        p.setId(id);
        return p;
    }

    public Category fromCategoryId(Long id) {
        if (id == null) return null;
        Category c = new Category();
        c.setId(id);
        return c;
    }

    public User fromUserId(Long id) {
        if (id == null) return null;
        User u = new User();
        u.setId(id);
        return u;
    }

    private Long safeGetId(Product p) {
        try { return p.getId(); } catch (Exception e) { return null; }
    }

    private Long safeGetId(Category c) {
        try { return c.getId(); } catch (Exception e) { return null; }
    }

    private Long safeGetId(User u) {
        try { return u.getId(); } catch (Exception e) { return null; }
    }
}