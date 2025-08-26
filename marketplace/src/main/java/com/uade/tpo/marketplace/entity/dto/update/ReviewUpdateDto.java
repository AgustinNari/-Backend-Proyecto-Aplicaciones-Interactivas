package com.uade.tpo.marketplace.entity.dto.update;

public record ReviewUpdateDto(
    Integer rating,
    String title,
    String comment,
    Boolean visible
) {}
