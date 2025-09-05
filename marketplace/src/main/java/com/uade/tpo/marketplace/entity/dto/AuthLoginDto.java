package com.uade.tpo.marketplace.entity.dto;


import jakarta.validation.constraints.NotBlank;

public record AuthLoginDto(
    @NotBlank String email,
    @NotBlank String password
) {}

//TODO: Borrar?