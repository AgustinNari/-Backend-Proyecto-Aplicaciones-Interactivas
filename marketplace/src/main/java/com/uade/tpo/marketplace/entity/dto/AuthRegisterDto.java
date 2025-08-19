package com.uade.tpo.marketplace.entity.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record AuthRegisterDto(
    @NotBlank @Size(min=3, max=50) String username,
    @NotBlank @Email String email,
    @NotBlank @Size(min=6, max=100) String password,
    @NotNull String role // "BUYER" ó "SELLER" (validar en service)
) {}
