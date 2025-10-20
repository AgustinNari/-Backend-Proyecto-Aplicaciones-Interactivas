package com.uade.tpo.marketplace.entity.dto.update;

import jakarta.validation.constraints.NotBlank;

public record CategoryUpdateDto(
    @NotBlank(message = "La descripción no puede estar vacía")
    String description
) {}