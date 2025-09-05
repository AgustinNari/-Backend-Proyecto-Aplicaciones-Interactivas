package com.uade.tpo.marketplace.entity.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

import com.uade.tpo.marketplace.entity.enums.DiscountScope;
import com.uade.tpo.marketplace.entity.enums.DiscountType;

public record DiscountResponseDto(
    Long id,
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
    Instant createdAt,
    Instant expiresAt,
    Long targetBuyerId
) {}