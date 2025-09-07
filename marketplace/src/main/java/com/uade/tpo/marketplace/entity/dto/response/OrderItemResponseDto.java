package com.uade.tpo.marketplace.entity.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record OrderItemResponseDto(
    Long id,
    Long productId,
    String productTitle,
    BigDecimal unitPrice,
    Integer quantity,
    BigDecimal lineSubtotal,
    BigDecimal discountAmount,
    BigDecimal lineTotal,
    List<OrderItemDigitalKeyResponseDto> digitalKeys
) {}