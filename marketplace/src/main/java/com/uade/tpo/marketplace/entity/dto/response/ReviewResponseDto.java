package com.uade.tpo.marketplace.entity.dto.response;

import java.time.Instant;

public record ReviewResponseDto(
    Long id,
    Long productId,
    Long buyerId,
    Integer rating,
    String title,
    String comment,
    boolean visible,
    Instant createdAt
) {}