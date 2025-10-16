package com.uade.tpo.marketplace.entity.dto.response;

import java.math.BigDecimal;

public record SellerResponseDto(
    Long id,
    String displayName,
    String sellerDescription,
    String firstName,
    String lastName,
    String email,
    String phone,
    String country,
    Boolean active,
    BigDecimal sellerRating,
    String avatarContentType,
    String avatarDataUrl
    //TODO: Agregar products acá???
) {

}
