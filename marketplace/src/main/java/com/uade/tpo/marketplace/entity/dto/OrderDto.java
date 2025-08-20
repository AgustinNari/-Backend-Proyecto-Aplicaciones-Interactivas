package com.uade.tpo.marketplace.entity.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderDto(
    Long id,
    Long buyerId,
    BigDecimal totalAmount,
    BigDecimal discountAmount,
    BigDecimal taxAmount,
    String status,
    List<OrderItemDto> items,
    Instant createdAt,
    Instant completedAt
) {}