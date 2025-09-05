package com.uade.tpo.marketplace.entity.dto.response;

import java.math.BigDecimal;

public record DiscountValidationResponseDto(
    boolean valid,
    BigDecimal discountAmount,
    String message
) {

}
