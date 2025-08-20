package com.uade.tpo.marketplace.entity.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record ProductUpdateDto(
    String title,
    String description,
    @DecimalMin("0.0") BigDecimal price,
    Set<Long> categoryIds,    // si está presente, reemplaza categorías actuales
    Boolean active,
    String platform,
    LocalDate releaseDate,
    @Size(max = 200) String developer,
    @Size(max = 200) String publisher,
    @Min(0) @Max(100) Integer metacriticScore
) {}