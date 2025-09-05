package com.uade.tpo.marketplace.entity.dto.response;

import java.time.Instant;

import com.uade.tpo.marketplace.entity.enums.Role;

public record SellerResponseDto(
    Long id,
    String displayName,
    String firstName,
    String lastName,
    String email,
    Role role,
    String phone,
    String country,
    Boolean active,
    Instant createdAt,
    Instant lastLogin,
    Integer sellerRating
    //TODO: Agregar products acá???
) {

}
