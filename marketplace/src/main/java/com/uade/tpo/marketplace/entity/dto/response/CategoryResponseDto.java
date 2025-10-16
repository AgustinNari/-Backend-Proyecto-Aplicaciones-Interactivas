package com.uade.tpo.marketplace.entity.dto.response;

public record CategoryResponseDto(
    Long id,
    String description,
    Integer productCount,
    Boolean featured
) {

    public String getDescription() {
        return this.description;
    }
}

