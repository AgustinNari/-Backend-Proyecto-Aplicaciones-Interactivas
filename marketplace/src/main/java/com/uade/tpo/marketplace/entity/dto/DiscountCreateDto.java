package com.uade.tpo.marketplace.entity.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;

public record DiscountCreateDto(
    String code,
    @NotNull String type, // PERCENT, FIXED, BULK
    @NotNull BigDecimal value,
    @NotNull String scope, // PRODUCT,CATEGORY,SELLER
    Integer targetId,
    Integer minQuantity,
    Instant startsAt,
    Instant endsAt,
    Integer maxUses,
    Integer perUserLimit
) {}