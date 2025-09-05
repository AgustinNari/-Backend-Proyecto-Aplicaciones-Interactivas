package com.uade.tpo.marketplace.entity.dto.update;

import jakarta.validation.constraints.NotBlank;

public record DigitalKeyUpdateDto(
    @NotBlank String keyMask,
    String status
) {}
