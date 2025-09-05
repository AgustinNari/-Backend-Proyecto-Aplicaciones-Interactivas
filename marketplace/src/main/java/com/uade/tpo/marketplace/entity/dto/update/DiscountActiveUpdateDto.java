package com.uade.tpo.marketplace.entity.dto.update;

import jakarta.validation.constraints.NotNull;

public record DiscountActiveUpdateDto(
    @NotNull Boolean active
) {

}
