package com.uade.tpo.marketplace.entity.dto.create;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductImageCreateDto(
    @NotNull
    Long productId,

    @NotBlank
    String name,

    @NotBlank
    MultipartFile file
) {}