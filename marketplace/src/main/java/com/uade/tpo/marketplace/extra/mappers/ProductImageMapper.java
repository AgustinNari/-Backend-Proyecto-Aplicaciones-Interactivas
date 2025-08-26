package com.uade.tpo.marketplace.extra.mappers;

import org.springframework.stereotype.Component;

import com.uade.tpo.marketplace.entity.basic.Product;
import com.uade.tpo.marketplace.entity.basic.ProductImage;
import com.uade.tpo.marketplace.entity.dto.create.ProductImageCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.ProductImageResponseDto;
import com.uade.tpo.marketplace.entity.dto.update.ProductImageUpdateDto;

@Component
public class ProductImageMapper {

    public ProductImage toEntity(ProductImageCreateDto dto){
        if (dto == null) return null;
        ProductImage img = new ProductImage();
        img.setUrl(dto.url());
        img.setAltText(dto.altText());
        img.setPrimary(Boolean.TRUE.equals(dto.isPrimary()));
        // Producto como placeholder (service debe cargar el producto real)
        if (dto.productId() != null) {
            Product p = new Product();
            p.setId(dto.productId());
            img.setProduct(p);
        }
        return img;
    }

    public void updateFromDto(ProductImageUpdateDto dto, ProductImage entity){
        if (dto == null || entity == null) return;
        if (dto.url() != null) entity.setUrl(dto.url());
        if (dto.altText() != null) entity.setAltText(dto.altText());
        if (dto.isPrimary() != null) entity.setPrimary(dto.isPrimary());
    }

    public ProductImageResponseDto toResponse(ProductImage img){
        if (img == null) return null;
        Long productId = img.getProduct() != null ? img.getProduct().getId() : null;
        return new ProductImageResponseDto(img.getId(), productId, img.getUrl(), img.getAltText(), img.isPrimary(), img.getCreatedAt());
    }

    public Product productFromId(Long id){
        if (id == null) return null;
        Product p = new Product();
        p.setId(id);
        return p;
    }
}