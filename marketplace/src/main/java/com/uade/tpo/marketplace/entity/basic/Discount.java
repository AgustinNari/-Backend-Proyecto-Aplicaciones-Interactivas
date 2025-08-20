package com.uade.tpo.marketplace.entity.basic;


import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

import com.uade.tpo.marketplace.entity.enums.DiscountScope;
import com.uade.tpo.marketplace.entity.enums.DiscountType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code; // Código de cupón
    private DiscountType type;
    private BigDecimal value;
    private DiscountScope scope;
    private Long targetId; // IdProducto, IdCategoría o IdVendedor
    private Integer minQuantity;
    private Instant startsAt;
    private Instant endsAt;
    private Integer maxUses;
    private Integer perUserLimit;
    private boolean active = true;
    private Instant createdAt = Instant.now();
}