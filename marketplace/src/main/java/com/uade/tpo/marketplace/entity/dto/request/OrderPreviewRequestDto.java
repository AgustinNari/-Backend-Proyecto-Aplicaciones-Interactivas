package com.uade.tpo.marketplace.entity.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;

public record OrderPreviewRequestDto(
    @NotEmpty List<OrderItemPreviewRequestDto> items
) {
}
