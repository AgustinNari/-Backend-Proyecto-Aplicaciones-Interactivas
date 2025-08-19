package com.uade.tpo.marketplace.entity.dto;

import java.time.Instant;
public record ReviewDto(
    Integer id,
    Integer productId,
    Integer buyerId,
    Integer rating,
    String title,
    String comment,
    boolean visible,
    Instant createdAt
) {}