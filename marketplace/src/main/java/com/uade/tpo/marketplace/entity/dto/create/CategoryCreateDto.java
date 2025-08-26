package com.uade.tpo.marketplace.entity.dto.create;

import jakarta.validation.constraints.NotBlank;

public record CategoryCreateDto(
    @NotBlank
    String description
) {}