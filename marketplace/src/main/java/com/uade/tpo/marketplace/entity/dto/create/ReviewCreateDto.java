package com.uade.tpo.marketplace.entity.dto.create;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ReviewCreateDto(
    @NotNull
    Long productId,

    @NotNull @Min(1) @Max(10)   
    Integer rating,

    String title,
    String comment,
    Long orderItemId 
) {}
