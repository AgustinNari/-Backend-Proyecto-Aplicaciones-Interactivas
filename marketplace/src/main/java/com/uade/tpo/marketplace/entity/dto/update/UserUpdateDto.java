package com.uade.tpo.marketplace.entity.dto.update;

public record UserUpdateDto(
    String email,
    String password,
    String displayName,
    String phone,
    String country,
    Boolean active
) {}