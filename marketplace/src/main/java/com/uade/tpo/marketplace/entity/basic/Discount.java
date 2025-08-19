package com.uade.tpo.marketplace.entity.basic;


import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

import com.uade.tpo.marketplace.entity.enums.DiscountScope;
import com.uade.tpo.marketplace.entity.enums.DiscountType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Discount {
    private Integer id;
    private String code; // Código de cupón
    private DiscountType type;
    private BigDecimal value;
    private DiscountScope scope;
    private Integer targetId; // IdProducto, IdCategoría o IdVendedor
    private Integer minQuantity;
    private Instant startsAt;
    private Instant endsAt;
    private Integer maxUses;
    private Integer perUserLimit;
    private boolean active = true;
    private Instant createdAt = Instant.now();
}