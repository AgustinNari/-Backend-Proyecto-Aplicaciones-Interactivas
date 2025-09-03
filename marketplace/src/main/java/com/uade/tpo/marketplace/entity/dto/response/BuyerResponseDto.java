package com.uade.tpo.marketplace.entity.dto.response;

import java.math.BigDecimal;
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
    Instant lastLogin,
    BigDecimal buyerBalance
    //TODO: Agregar orders acá???
    ) {

}
