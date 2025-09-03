package com.uade.tpo.marketplace.entity.dto.update;

import java.math.BigDecimal;
import java.time.Instant;

public record DiscountUpdateDto(
    String code,
    String type,
    BigDecimal value,
    String scope,
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
    Instant expiresAt
) {}