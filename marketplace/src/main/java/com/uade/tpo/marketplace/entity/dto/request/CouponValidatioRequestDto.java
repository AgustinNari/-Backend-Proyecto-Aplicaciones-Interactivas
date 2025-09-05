package com.uade.tpo.marketplace.entity.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CouponValidatioRequestDto(
     @NotNull Long orderOrOrderItemId,
     @NotBlank String code
) {

}
