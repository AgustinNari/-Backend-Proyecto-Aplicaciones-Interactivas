package com.uade.tpo.marketplace.entity.dto.response;

public record PasswordChangeResponseDto(
    boolean success,
    String message,
    Long userId
) {
}
