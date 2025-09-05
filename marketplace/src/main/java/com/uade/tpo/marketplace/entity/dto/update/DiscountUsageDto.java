package com.uade.tpo.marketplace.entity.dto.update;

import java.util.List;

import com.uade.tpo.marketplace.entity.dto.create.OrderItemCreateDto;

import jakarta.validation.constraints.NotNull;

public record DiscountUsageDto(
    @NotNull List<OrderItemCreateDto> items
) {

}
