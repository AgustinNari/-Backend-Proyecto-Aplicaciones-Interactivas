package com.uade.tpo.marketplace.entity.dto.request;

import jakarta.validation.constraints.NotNull;

public record DiscountValidatioRequestDto(
    @NotNull Long orderOrOrderItemId
) {

}
