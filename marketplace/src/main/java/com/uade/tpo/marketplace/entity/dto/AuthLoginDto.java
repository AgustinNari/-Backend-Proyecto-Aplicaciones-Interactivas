package com.uade.tpo.marketplace.entity.dto;


import jakarta.validation.constraints.*;

public record AuthLoginDto(
    @NotBlank String usernameOrEmail,
    @NotBlank String password
) {}