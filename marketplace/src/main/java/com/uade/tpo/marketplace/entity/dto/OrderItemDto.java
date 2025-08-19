package com.uade.tpo.marketplace.entity.dto;

import java.math.BigDecimal;

public record OrderItemDto(
    Integer productId,
    String productTitle,
    BigDecimal unitPrice,
    Integer quantity,
    String keyCode
) {}