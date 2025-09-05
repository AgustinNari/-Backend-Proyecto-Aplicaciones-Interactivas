package com.uade.tpo.marketplace.entity.dto.update;

import jakarta.validation.constraints.NotNull;

public record DiscountUsageDto(
    @NotNull Long orderOrOrderItemId
) {

}
