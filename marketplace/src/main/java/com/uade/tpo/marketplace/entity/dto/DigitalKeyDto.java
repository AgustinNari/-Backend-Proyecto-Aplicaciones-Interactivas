package com.uade.tpo.marketplace.entity.dto;

import java.time.Instant;

public record DigitalKeyDto(
    Long id,
    Long productId,
    String keyMask,
    String status,
    Instant createdAt,
    Instant reservedAt,
    Instant soldAt
) {}