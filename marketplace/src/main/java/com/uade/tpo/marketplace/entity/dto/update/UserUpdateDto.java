package com.uade.tpo.marketplace.entity.dto.update;

public record UserUpdateDto(
    String displayName,
    String firstName,
    String lastName,
    String phone,
    String sellerDescription,
    String country
) {}