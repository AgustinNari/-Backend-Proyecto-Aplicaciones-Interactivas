package com.uade.tpo.marketplace.entity.dto.response;

public record ProductImageResponseDto(
    Long id,
    Long productId,
    String name,
    String file
) {

    public ProductImageResponseDto (Long id, Long productId, String name, String file) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.file = file;
    }
}
