package com.uade.tpo.marketplace.entity.dto.create;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;

public record OrderCreateDto(
    @NotEmpty
    List<OrderItemCreateDto> items,

    String couponCode,
    String notes
) {}