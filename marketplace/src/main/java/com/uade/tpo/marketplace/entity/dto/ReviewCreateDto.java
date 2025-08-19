package com.uade.tpo.marketplace.entity.dto;


import jakarta.validation.constraints.*;

public record ReviewCreateDto(
    @NotNull Integer productId,
    @NotNull Integer rating,
    String title,
    String comment
) {}