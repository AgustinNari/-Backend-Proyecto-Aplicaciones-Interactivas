package com.uade.tpo.marketplace.entity.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.uade.tpo.marketplace.entity.enums.OrderStatus;


public record OrderResponseDto(
    Long id,
    Long buyerId,
    BigDecimal subtotal,
    BigDecimal totalAmount,
    BigDecimal discountAmount,
    OrderStatus status,
    List<OrderItemResponseDto> items,
    Instant createdAt,
    Instant completedAt,
    String notes
) {}