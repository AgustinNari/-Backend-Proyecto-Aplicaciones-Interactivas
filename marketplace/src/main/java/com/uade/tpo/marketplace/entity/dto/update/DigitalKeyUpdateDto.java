package com.uade.tpo.marketplace.entity.dto.update;

public record DigitalKeyUpdateDto(
    Long id,
    String keyMask,
    String status
) {}
