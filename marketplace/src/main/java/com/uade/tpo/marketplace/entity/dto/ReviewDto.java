package com.uade.tpo.marketplace.entity.dto;

import java.time.Instant;
public record ReviewDto(
    Long id,
    Long productId,
    Long buyerId,
    Integer rating,
    String title,
    String comment,
    boolean visible,
    Instant createdAt
) {}