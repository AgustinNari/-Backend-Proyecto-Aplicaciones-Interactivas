package com.uade.tpo.marketplace.entity.dto;

import jakarta.validation.constraints.*;

public record OrderItemCreateDto(
    @NotNull Long productId,
    @NotNull @Min(1) Integer quantity
) {}