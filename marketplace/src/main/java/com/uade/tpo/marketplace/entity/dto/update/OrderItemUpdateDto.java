package com.uade.tpo.marketplace.entity.dto.update;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

public record OrderItemUpdateDto(
    @Min(value = 1, message = "La cantidad debe ser al menos 1.")
    Integer quantity,
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio unitario debe ser mayor que 0.")
    BigDecimal unitPrice,
    Long discountId
) {}