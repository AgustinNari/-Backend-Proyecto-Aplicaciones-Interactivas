package com.uade.tpo.marketplace.entity.dto.update;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record ProductUpdateDto(
    String sku,
    String title,
    String description,
    BigDecimal price,
    String currency,
    Set<Long> categoryIds,
    String platform,
    String region,
    Integer minPurchaseQuantity,
    Integer maxPurchaseQuantity,
    LocalDate releaseDate,
    String developer,
    String publisher,
    Integer metacriticScore,
    Boolean active
) {}