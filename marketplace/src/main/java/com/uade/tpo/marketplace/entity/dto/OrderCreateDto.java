package com.uade.tpo.marketplace.entity.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public record OrderCreateDto(
    @NotEmpty List<OrderItemCreateDto> items,
    String couponCode
) {}
