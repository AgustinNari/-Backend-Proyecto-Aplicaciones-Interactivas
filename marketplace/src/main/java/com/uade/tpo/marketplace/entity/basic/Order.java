package com.uade.tpo.marketplace.entity.basic;


import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.uade.tpo.marketplace.entity.enums.OrderStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Integer id;
    private Integer buyerId;
    private BigDecimal totalAmount = BigDecimal.ZERO;
    private BigDecimal discountAmount = BigDecimal.ZERO;
    private BigDecimal taxAmount = BigDecimal.ZERO;
    private OrderStatus status = OrderStatus.PENDING;
    private Integer discountId;
    private Instant createdAt = Instant.now();
    private Instant completedAt;
    private List<OrderItem> items;
    private String notes;
}