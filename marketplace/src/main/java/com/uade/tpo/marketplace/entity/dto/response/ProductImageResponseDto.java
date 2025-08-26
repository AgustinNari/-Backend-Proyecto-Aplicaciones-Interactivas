package com.uade.tpo.marketplace.entity.dto.response;

import java.time.Instant;

public record ProductImageResponseDto(
    Long id,
    Long productId,
    String url,
    String altText,
    boolean isPrimary,
    Instant createdAt
) {}
