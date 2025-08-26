package com.uade.tpo.marketplace.entity.dto.response;

import java.time.Instant;

public record DigitalKeyResponseDto(
    Long id,
    Long productId,
    String keyMask,
    String status,
    Instant soldAt,
    Instant createdAt,
    String KeyCode
) { }
