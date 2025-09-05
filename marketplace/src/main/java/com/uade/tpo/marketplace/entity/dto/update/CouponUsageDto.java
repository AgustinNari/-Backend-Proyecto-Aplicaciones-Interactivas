package com.uade.tpo.marketplace.entity.dto.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CouponUsageDto(
    @NotNull Long orderOrOrderItemId,
    @NotBlank String code
    
) {

}
