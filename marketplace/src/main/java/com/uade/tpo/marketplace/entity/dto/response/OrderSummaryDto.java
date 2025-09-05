package com.uade.tpo.marketplace.entity.dto.response;

import java.math.BigDecimal;

public record OrderSummaryDto(
    BigDecimal subtotal,       
    BigDecimal discountAmount, 
    BigDecimal totalAmount,
    DiscountAppliedDto discountsApplied
) {

}
