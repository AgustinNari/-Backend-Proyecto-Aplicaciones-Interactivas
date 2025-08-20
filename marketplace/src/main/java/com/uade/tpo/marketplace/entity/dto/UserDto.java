package com.uade.tpo.marketplace.entity.dto;


import java.time.Instant;

public record UserDto(
    Long id,
    String username,
    String email,
    String displayName,
    String phone,
    String role,
    boolean active,
    Instant createdAt
) {}