package com.uade.tpo.marketplace.entity.dto;

public record AuthResponseDto(
    String accessToken,
    String tokenType,
    Integer userId,
    String username,
    String role
) {}