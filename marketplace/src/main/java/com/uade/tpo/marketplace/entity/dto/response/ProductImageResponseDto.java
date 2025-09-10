package com.uade.tpo.marketplace.entity.dto.response;

public record ProductImageResponseDto(
    Long id,
    Long productId,
    String name,
    boolean isPrimary,
    String file
) {

    public ProductImageResponseDto (Long id, Long productId, String name,boolean isPrimary, String file) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.isPrimary = isPrimary;
        this.file = file;

    }
}
