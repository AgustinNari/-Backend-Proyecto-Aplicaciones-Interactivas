package com.uade.tpo.marketplace.entity.dto;

import com.uade.tpo.marketplace.entity.enums.Role;

public record AuthResponseDto(
    String accessToken,
    String tokenType,
    Long userId,
    String email,
    Role role
) {}


//TODO: Borrar?