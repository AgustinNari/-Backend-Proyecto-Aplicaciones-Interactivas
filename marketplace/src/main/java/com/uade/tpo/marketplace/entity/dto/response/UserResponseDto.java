package com.uade.tpo.marketplace.entity.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

public record UserResponseDto(
    Long id,
    String username,
    String email,
    String role,
    String displayName,
    String phone,
    String country,
    boolean active,
    Instant createdAt,
    Instant lastLogin,
    BigDecimal sellerBalance,
    Integer productCount
) {}