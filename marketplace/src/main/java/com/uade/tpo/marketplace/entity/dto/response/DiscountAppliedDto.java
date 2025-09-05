package com.uade.tpo.marketplace.entity.dto.response;

import java.math.BigDecimal;

public record DiscountAppliedDto(
    Long discountId,
    String code,
    String type,       
    BigDecimal amount, 
    String scope,      
    Long targetProductId,
    Long targetCategoryId,
    String message
) {

}
