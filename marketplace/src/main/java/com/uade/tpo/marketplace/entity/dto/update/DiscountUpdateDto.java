package com.uade.tpo.marketplace.entity.dto.update;

import java.math.BigDecimal;
import java.time.Instant;

import com.uade.tpo.marketplace.entity.enums.DiscountScope;
import com.uade.tpo.marketplace.entity.enums.DiscountType;

public record DiscountUpdateDto(
    String code,
    DiscountType type,
    BigDecimal value,
    DiscountScope scope,
    Long targetProductId,
    Long targetCategoryId,
    Long targetSellerId,
    Integer minQuantity,
    Instant startsAt,
    Instant endsAt,
    BigDecimal minPrice,
    BigDecimal maxPrice,
    Integer maxUses,
    Integer perUserLimit,
    Boolean active,
    Instant expiresAt,
    Long targetBuyerId
) {}