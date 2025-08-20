package com.uade.tpo.marketplace.entity.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record ProductCreateDto(
    @NotBlank String title,
    String description,
    @NotNull @DecimalMin("0.0") BigDecimal price,
    @NotEmpty Set<Long> categoryIds,   // ids de categorías
    String platform,
    LocalDate releaseDate,
    @Size(max = 200) String developer,
    @Size(max = 200) String publisher,
    @Min(0) @Max(100) Integer metacriticScore
) {}
