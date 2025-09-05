package com.uade.tpo.marketplace.entity.dto.response;

import java.time.Instant;

import com.uade.tpo.marketplace.entity.enums.KeyStatus;

public record DigitalKeyResponseDto(
    Long id,
    Long productId,
    String keyMask,
    KeyStatus status,
    Instant soldAt,
    Instant createdAt,
    String keyCode
) { }
