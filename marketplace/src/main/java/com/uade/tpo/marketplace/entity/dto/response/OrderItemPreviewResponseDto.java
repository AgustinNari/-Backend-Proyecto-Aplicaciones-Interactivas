package com.uade.tpo.marketplace.entity.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record OrderItemPreviewResponseDto(
    Long productId,
    String productTitle,
    String sku,
    BigDecimal unitPrice,
    Integer quantity,
    Integer availableStock,
    boolean isAvailable,              // Revisión de stock
    BigDecimal lineSubtotal,        
    BigDecimal discountAmount,        
    BigDecimal lineTotal,           
    DiscountAppliedDto discountsApplied, 
    List<String> warnings
) {

}
