package com.uade.tpo.marketplace.entity.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;

public record ProductDetailResponseDto(
    Long id,
    Long sellerId,
    String sku,
    String sellerDisplayName,

    String title,
    String description,
    BigDecimal price,
    boolean active,
    String platform,
    String region,
    Integer minPurchaseQuantity,
    Integer maxPurchaseQuantity,

    LocalDate releaseDate,
    String developer,
    String publisher,
    Integer metacriticScore,
    boolean featured,

    List<CategoryResponseDto> categories,

    DiscountResponseDto bestDiscount,

    List<ProductImageResponseDto> images,

    Page<ReviewResponseDto> reviewsPage,
    Double avgRating,
    Long ratingCount,
    Integer stock,
    Integer sold,
    Long amountSold
) {}