package com.uade.tpo.marketplace.entity.dto.create;

import java.math.BigDecimal;
import java.time.Instant;

import com.uade.tpo.marketplace.entity.enums.DiscountScope;
import com.uade.tpo.marketplace.entity.enums.DiscountType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DiscountCreateDto(
    @NotBlank
    String code,

    @NotNull
    DiscountType type,     

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    BigDecimal value,

    @NotNull
    DiscountScope scope,   

    Long targetProductId,
    Long targetCategoryId,
    Long targetSellerId,
    Integer minQuantity,
    Instant startsAt,
    Instant endsAt,
    BigDecimal minPrice,
    BigDecimal maxPrice,
    Instant expiresAt,
    Long targetBuyerId
) {}