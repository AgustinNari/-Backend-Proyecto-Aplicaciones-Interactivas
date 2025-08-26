package com.uade.tpo.marketplace.entity.dto.create;

import jakarta.validation.constraints.NotNull;

public record ReviewCreateDto(
    @NotNull
    Long productId,

    @NotNull
    Integer rating,

    String title,
    String comment,
    Long orderItemId 
) {}
