package com.uade.tpo.marketplace.entity.dto.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DigitalKeyCreateDto(
    @NotNull
    Long productId,
    @NotBlank
    String keyCode,    
    String keyMask     
) {}