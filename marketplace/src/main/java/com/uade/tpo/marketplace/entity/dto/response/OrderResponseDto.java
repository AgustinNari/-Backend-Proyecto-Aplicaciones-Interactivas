package com.uade.tpo.marketplace.entity.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponseDto(
    Long id,
    Long buyerId,
    BigDecimal subtotal,
    BigDecimal totalAmount,
    BigDecimal discountAmount,
    BigDecimal taxAmount,
    String status,
    List<OrderItemResponseDto> items,
    Instant createdAt,
    Instant completedAt,
    String notes
) {}