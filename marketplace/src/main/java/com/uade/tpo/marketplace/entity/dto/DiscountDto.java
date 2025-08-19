package com.uade.tpo.marketplace.entity.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record DiscountDto(
    Integer id,
    String code,
    String type,
    BigDecimal value,
    String scope,
    Integer targetId,
    Integer minQuantity,
    Instant startsAt,
    Instant endsAt,
    Integer maxUses,
    Integer perUserLimit,
    boolean active
) {}