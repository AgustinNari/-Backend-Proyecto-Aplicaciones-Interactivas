package com.uade.tpo.marketplace.entity.dto.response;

public record ProductImageDeletionResponseDto(
    boolean success,
    Long deletedImageId,
    String message
) {

}
