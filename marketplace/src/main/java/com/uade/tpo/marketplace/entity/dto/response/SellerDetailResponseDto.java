package com.uade.tpo.marketplace.entity.dto.response;

public record SellerDetailResponseDto(
    Long id,
    String displayName,
    String avatarContentType,
    String avatarDataUrl,

    String sellerDescription,
    String firstName,
    String lastName,
    String email,
    String phone,
    String country,

    Double avgRating,
    Long ratingCount,
    Long soldKeys,
    Long amountSold
) {}