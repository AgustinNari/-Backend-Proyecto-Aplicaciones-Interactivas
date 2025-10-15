package com.uade.tpo.marketplace.entity.dto.response;

import java.math.BigDecimal;

public record CouponValidationResponseDto(
    boolean isValid,
    String message,
    BigDecimal discountAmount,
    BigDecimal newTotalPrice, 
    Long discountId,
    Long productId
) {}
