package com.uade.tpo.marketplace.entity.dto.update;

import java.util.List;

import com.uade.tpo.marketplace.entity.dto.create.OrderItemCreateDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CouponUsageDto(
    @NotNull List<OrderItemCreateDto> items,
    @NotBlank String code
    
) {

}
