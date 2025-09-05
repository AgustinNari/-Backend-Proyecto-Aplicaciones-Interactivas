package com.uade.tpo.marketplace.entity.dto.update;

import com.uade.tpo.marketplace.entity.enums.OrderStatus;

public record OrderUpdateDto(
    OrderStatus status,
    String notes
) {}