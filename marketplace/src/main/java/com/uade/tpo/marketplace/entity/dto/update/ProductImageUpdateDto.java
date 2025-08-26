package com.uade.tpo.marketplace.entity.dto.update;

public record ProductImageUpdateDto(
    String url,
    String altText,
    Boolean isPrimary
) {}
