package com.uade.tpo.marketplace.entity.dto.response;

public record ReviewDeletionResponseDto(
    boolean success,
    Long reviewId,
    String message
) {
}
