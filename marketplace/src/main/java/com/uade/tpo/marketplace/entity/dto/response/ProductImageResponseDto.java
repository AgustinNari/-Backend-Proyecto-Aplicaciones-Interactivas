package com.uade.tpo.marketplace.entity.dto.response;

public record ProductImageResponseDto(
    Long id,
    Long productId,
    String name,
    boolean isPrimary,
    String file,
    String contentType,
    String dataUrl
) {

    }