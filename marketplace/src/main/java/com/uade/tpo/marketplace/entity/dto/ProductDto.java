package com.uade.tpo.marketplace.entity.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

import com.uade.tpo.marketplace.entity.basic.Category;

public record ProductDto(
    Integer id,
    Integer sellerId,
    String sku,
    String title,
    String description,
    BigDecimal price,
    String currency,
    Set<Category> categories,
    boolean active,
    Integer availableStock,
    String platform,
    LocalDate releaseDate,
    String developer,
    String publisher,
    Integer metacriticScore,
    Instant createdAt,
    Instant updatedAt
) {

    
    

}