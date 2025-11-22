package com.uade.tpo.marketplace.entity.dto.response;

import java.time.Instant;

public record LatestReviewResponseDto(
    Long id,
    Integer rating,
    String title,
    String comment,
    Instant createdAt,
    
    // Información del producto
    Long productId,
    String productTitle,
    String productImageDataUrl, // URL de la imagen principal del producto
    
    // Información del usuario
    Long buyerId,
    String buyerDisplayName
) {}