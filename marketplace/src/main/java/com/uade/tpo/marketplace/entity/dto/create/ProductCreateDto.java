package com.uade.tpo.marketplace.entity.dto.create;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ProductCreateDto(
    @NotNull
    Long sellerId,

    String sku,

    @NotBlank
    String title,

    String description,

    @NotNull @DecimalMin(value = "0.0", inclusive = true)
    BigDecimal price,

    String currency,

    @NotEmpty
    Set<Long> categoryIds,

    @NotBlank
    String platform,

    @NotBlank
    String region,

    @Min(1)
    Integer minPurchaseQuantity,

    @Min(1)
    Integer maxPurchaseQuantity,

    LocalDate releaseDate,
    String developer,
    String publisher,

    @Min(0) @Max(100)
    Integer metacriticScore
) {}