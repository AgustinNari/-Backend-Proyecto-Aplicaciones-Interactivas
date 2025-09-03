package com.uade.tpo.marketplace.entity.dto.create;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserCreateDto(

    @NotBlank
    String displayName,

    @NotBlank
    String firstName,

    @NotBlank
    String lastName,

    @NotBlank @Email
    String email,

    @NotBlank @Size(min = 6)
    String password,

    @NotNull
    String role,


    String phone,

    @NotBlank
    String country
) {}
