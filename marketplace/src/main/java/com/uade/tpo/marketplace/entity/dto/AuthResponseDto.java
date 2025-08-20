package com.uade.tpo.marketplace.entity.dto;

public record AuthResponseDto(
    String accessToken,
    String tokenType,
    Long userId,
    String username,
    String role
) {}