package com.uade.tpo.marketplace.entity.dto.response;

import java.time.Instant;

public record BuyerResponseDto(    
    Long id,
    String displayName,
    String firstName,
    String lastName,
    String email,
    String role,
    String phone,
    String country,
    boolean active,
    Instant createdAt,
    Instant lastLogin
    //TODO: Agregar orders acá???
    ) {

}
