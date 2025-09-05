package com.uade.tpo.marketplace.entity.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemPreviewRequestDto(
    @NotNull Long productId,
    @NotNull @Min(1) Integer quantity,
    String itemCouponCode,
    Long discountId
) {

}
