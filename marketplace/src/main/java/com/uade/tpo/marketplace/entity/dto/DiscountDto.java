package com.uade.tpo.marketplace.entity.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record DiscountDto(
    Long id,
    String code,
    String type,
    BigDecimal value,
    String scope,
    Long targetId,
    Integer minQuantity,
    Instant startsAt,
    Instant endsAt,
    Integer maxUses,
    Integer perUserLimit,
    boolean active
) {}