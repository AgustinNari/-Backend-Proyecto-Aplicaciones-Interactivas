package com.uade.tpo.marketplace.entity.dto.response;

import java.time.Instant;

public record OrderKeyResponseDto(
    Long id,
    String keyCode,
    String status,
    Instant soldAt,
    Long orderItemId,
    Long orderId,
    Long productId,
    String productTitle
) {}