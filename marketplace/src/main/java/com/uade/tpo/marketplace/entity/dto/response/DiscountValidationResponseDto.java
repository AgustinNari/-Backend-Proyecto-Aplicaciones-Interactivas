package com.uade.tpo.marketplace.entity.dto.response;

import java.math.BigDecimal;

public record DiscountValidationResponseDto(
    Boolean valid,
    BigDecimal discountAmount,
    String message
) {

}
