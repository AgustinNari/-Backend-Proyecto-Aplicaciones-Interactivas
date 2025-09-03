package com.uade.tpo.marketplace.entity.dto.create;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DiscountCreateDto(
    @NotBlank
    String code,

    @NotNull
    String type,     

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    BigDecimal value,

    @NotNull
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