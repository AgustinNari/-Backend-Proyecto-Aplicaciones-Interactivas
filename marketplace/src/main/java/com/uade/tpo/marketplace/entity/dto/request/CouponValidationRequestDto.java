package com.uade.tpo.marketplace.entity.dto.request;

import com.uade.tpo.marketplace.entity.dto.create.OrderItemCreateDto;

public record CouponValidationRequestDto(
    String code,
    Long buyerId,
    OrderItemCreateDto item
) {}