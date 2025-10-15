package com.uade.tpo.marketplace.entity.dto.request;

import java.util.List;

import com.uade.tpo.marketplace.entity.dto.create.OrderItemCreateDto;

public record CouponValidationBulkRequestDto(
    String code,
    Long buyerId,
    List<OrderItemCreateDto> items
) {}
