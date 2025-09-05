package com.uade.tpo.marketplace.entity.dto.update;

import com.uade.tpo.marketplace.entity.enums.KeyStatus;

import jakarta.validation.constraints.NotBlank;

public record DigitalKeyUpdateDto(
    @NotBlank String keyMask,
    KeyStatus status
) {}
