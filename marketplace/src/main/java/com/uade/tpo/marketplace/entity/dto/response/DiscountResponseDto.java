package com.uade.tpo.marketplace.entity.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

public record DiscountResponseDto(
    Long id,
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
    Integer maxUses,
    Integer perUserLimit,
    boolean active,
    Instant createdAt
) {}