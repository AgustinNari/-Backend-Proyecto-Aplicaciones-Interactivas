package com.uade.tpo.marketplace.entity.dto.response;

import java.time.Instant;
import java.util.List;

public record LatestReviewResponseDto(
    Long id,
    Integer rating,
    String title,
    String comment,
    Instant createdAt,
    
    // Información del producto
    Long productId,
    String productTitle,
    String productImageDataUrl,
    
    // Información del usuario
    Long buyerId,
    String buyerDisplayName,
    
    // Categorías del producto
    List<String> productCategories
) {}