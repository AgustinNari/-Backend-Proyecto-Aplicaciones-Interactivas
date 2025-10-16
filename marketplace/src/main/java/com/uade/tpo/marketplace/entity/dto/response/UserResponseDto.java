package com.uade.tpo.marketplace.entity.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

import com.uade.tpo.marketplace.entity.enums.Role;
public record UserResponseDto(
    Long id,
    String displayName,
    String sellerDescription,
    String firstName,
    String lastName,
    String email,
    Role role,
    String phone,
    String country,
    Boolean active,
    Instant createdAt,
    Instant lastLogin,
    BigDecimal buyerBalance,
    String avatarContentType,
    String avatarDataUrl
) {}