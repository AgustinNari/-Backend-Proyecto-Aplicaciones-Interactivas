package com.uade.tpo.marketplace.entity.dto.response;

import java.math.BigDecimal;

public record OrderItemResponseDto(
    Long id,
    Long productId,
    String productTitle,
    BigDecimal unitPrice,
    Integer quantity,
    BigDecimal lineTotal,
    String keyCode,
    String keyMask
) {}