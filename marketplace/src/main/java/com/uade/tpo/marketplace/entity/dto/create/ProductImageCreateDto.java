package com.uade.tpo.marketplace.entity.dto.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductImageCreateDto(
    @NotNull
    Long productId,

    @NotBlank
    String url,

    String altText,

    Boolean isPrimary
) {}