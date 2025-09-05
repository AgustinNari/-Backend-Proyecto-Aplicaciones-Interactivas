package com.uade.tpo.marketplace.entity.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record ProductResponseDto(
    Long id,
    Long sellerId,
    String sellerDisplayName,
    String sku,
    String title,
    String description,
    BigDecimal price,
    String currency,
    Set<CategoryResponseDto> categories,
    Boolean active,
    Instant createdAt,
    Instant updatedAt,
    String platform,
    String region,
    Integer minPurchaseQuantity,
    Integer maxPurchaseQuantity,
    LocalDate releaseDate,
    String developer,
    String publisher,
    Integer metacriticScore,
    Integer availableStock, 
    List<String> imageUrls
) {}