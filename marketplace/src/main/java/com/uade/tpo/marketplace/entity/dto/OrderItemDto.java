package com.uade.tpo.marketplace.entity.dto;

import java.math.BigDecimal;

public record OrderItemDto(
    Long productId,
    String productTitle,
    BigDecimal unitPrice,
    Integer quantity,
    String keyCode
) {}