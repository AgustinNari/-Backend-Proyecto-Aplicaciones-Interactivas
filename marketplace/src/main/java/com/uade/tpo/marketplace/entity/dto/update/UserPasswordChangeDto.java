package com.uade.tpo.marketplace.entity.dto.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserPasswordChangeDto(
    @NotBlank String currentPassword,
    @NotBlank @Size(min=6) String newPassword
) {}