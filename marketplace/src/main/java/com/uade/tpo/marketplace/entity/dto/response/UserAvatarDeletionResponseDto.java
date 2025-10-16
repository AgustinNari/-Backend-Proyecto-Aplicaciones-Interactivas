package com.uade.tpo.marketplace.entity.dto.response;

public record UserAvatarDeletionResponseDto(
    boolean success,
    Long userId,
    String message
) {}
