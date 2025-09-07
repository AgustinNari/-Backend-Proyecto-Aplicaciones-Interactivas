package com.uade.tpo.marketplace.entity.dto.create;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemCreateDto(
    @NotNull
    Long productId,

    String couponCode,

    @NotNull @Min(1)
    Integer quantity
) {}