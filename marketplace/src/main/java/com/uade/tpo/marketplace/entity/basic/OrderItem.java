package com.uade.tpo.marketplace.entity.basic;

import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    private Integer id;
    private Integer orderId;
    private Integer productId;
    private Integer digitalKeyId;
    private Integer quantity = 1;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;
    private Instant createdAt = Instant.now();
}