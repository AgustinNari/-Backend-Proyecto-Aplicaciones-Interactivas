package com.uade.tpo.marketplace.entity.dto;

import java.time.Instant;

public record DigitalKeyDto(
    Integer id,
    Integer productId,
    String keyMask,
    String status,
    Instant createdAt,
    Instant reservedAt,
    Instant soldAt
) {}