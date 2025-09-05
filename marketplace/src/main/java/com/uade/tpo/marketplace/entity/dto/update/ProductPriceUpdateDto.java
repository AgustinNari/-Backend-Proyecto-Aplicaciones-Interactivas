package com.uade.tpo.marketplace.entity.dto.update;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record ProductPriceUpdateDto(
    @NotNull @DecimalMin("0.0") BigDecimal price
) {

}
