package com.uade.tpo.marketplace.entity.dto;


import java.time.Instant;

public record ErrorDto(
    String code,
    String message,
    Instant timestamp
) {}