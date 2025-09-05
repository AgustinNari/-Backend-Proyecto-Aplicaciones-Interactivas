package com.uade.tpo.marketplace.entity.dto.response;

import java.time.Instant;
import java.util.List;

public record OrderPreviewResponseDto(
    OrderSummaryDto summary,
    List<OrderItemPreviewResponseDto> items,
    boolean valid,
    List<String> errors,
    List<String> warnings,
    Instant generatedAt
) {

}
