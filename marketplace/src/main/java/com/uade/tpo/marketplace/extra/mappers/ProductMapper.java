package com.uade.tpo.marketplace.extra.mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.uade.tpo.marketplace.entity.basic.Category;
import com.uade.tpo.marketplace.entity.basic.Product;
import com.uade.tpo.marketplace.entity.basic.ProductImage;
import com.uade.tpo.marketplace.entity.basic.User;
import com.uade.tpo.marketplace.entity.dto.create.ProductCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.CategoryResponseDto;
import com.uade.tpo.marketplace.entity.dto.response.ProductResponseDto;
import com.uade.tpo.marketplace.entity.dto.update.ProductUpdateDto;

@Component
public class ProductMapper {

    private final CategoryMapper categoryMapper;
    private final ProductImageMapper imageMapper;

    public ProductMapper(CategoryMapper categoryMapper, ProductImageMapper imageMapper) {
        this.categoryMapper = categoryMapper;
        this.imageMapper = imageMapper;
    }

    public Product toEntity(ProductCreateDto dto){
        if (dto == null) return null;
        Product p = new Product();
        if (dto.sellerId() != null) {
            User seller = new User();
            seller.setId(dto.sellerId());
            p.setSeller(seller); 
        }
        p.setSku(dto.sku());
        p.setTitle(dto.title());
        p.setDescription(dto.description());
        p.setPrice(dto.price());
        p.setCurrency(dto.currency());
        p.setPlatform(dto.platform());
        p.setRegion(dto.region());
        p.setMinPurchaseQuantity(dto.minPurchaseQuantity() == null ? 1 : dto.minPurchaseQuantity());
        p.setMaxPurchaseQuantity(dto.maxPurchaseQuantity() == null ? 1000 : dto.maxPurchaseQuantity());
        p.setReleaseDate(dto.releaseDate());
        p.setDeveloper(dto.developer());
        p.setPublisher(dto.publisher());
        p.setMetacriticScore(dto.metacriticScore());
        if (dto.categoryIds() != null) {
            Set<Category> cats = dto.categoryIds().stream().map(id -> {
                Category c = new Category();
                c.setId(id);
                return c;
            }).collect(Collectors.toSet());
            p.setCategories(cats);
        }
        return p;
    }

    public void updateFromDto(ProductUpdateDto dto, Product entity){
        if (dto == null || entity == null) return;
        if (dto.sku() != null) entity.setSku(dto.sku());
        if (dto.title() != null) entity.setTitle(dto.title());
        if (dto.description() != null) entity.setDescription(dto.description());
        if (dto.price() != null) entity.setPrice(dto.price());
        if (dto.currency() != null) entity.setCurrency(dto.currency());
        if (dto.platform() != null) entity.setPlatform(dto.platform());
        if (dto.region() != null) entity.setRegion(dto.region());
        if (dto.minPurchaseQuantity() != null) entity.setMinPurchaseQuantity(dto.minPurchaseQuantity());
        if (dto.maxPurchaseQuantity() != null) entity.setMaxPurchaseQuantity(dto.maxPurchaseQuantity());
        if (dto.releaseDate() != null) entity.setReleaseDate(dto.releaseDate());
        if (dto.developer() != null) entity.setDeveloper(dto.developer());
        if (dto.publisher() != null) entity.setPublisher(dto.publisher());
        if (dto.metacriticScore() != null) entity.setMetacriticScore(dto.metacriticScore());
        if (dto.categoryIds() != null) {
            Set<Category> cats = dto.categoryIds().stream().map(id -> {
                Category c = new Category();
                c.setId(id);
                return c;
            }).collect(Collectors.toSet());
            entity.setCategories(cats);
        }
        if (dto.active() != null) entity.setActive(dto.active());
    }

 
    public ProductResponseDto toResponse(Product product){
        if (product == null) return null;
        Set<CategoryResponseDto> categoryDtos = product.getCategories() == null ? Set.of() :
            product.getCategories().stream().map(categoryMapper::toResponse).collect(Collectors.toSet());
      
        List<String> imageUrls = product.getImages() == null ? java.util.List.of() :
            product.getImages().stream().map(ProductImage::getUrl).collect(Collectors.toList());
        Long sellerId = product.getSeller() != null ? product.getSeller().getId() : null;
        return new ProductResponseDto(
            product.getId(),
            sellerId,
            product.getSku(),
            product.getTitle(),
            product.getDescription(),
            product.getPrice(),
            product.getCurrency(),
            categoryDtos,
            product.isActive(),
            product.getCreatedAt(),
            product.getUpdatedAt(),
            product.getPlatform(),
            product.getRegion(),
            product.getMinPurchaseQuantity(),
            product.getMaxPurchaseQuantity(),
            product.getReleaseDate(),
            product.getDeveloper(),
            product.getPublisher(),
            product.getMetacriticScore(),
            null, 
            imageUrls
        );
    }

   
    public ProductResponseDto toResponse(Product product, int availableStock){
        ProductResponseDto dto = toResponse(product);
        return new ProductResponseDto(
            dto.id(),
            dto.sellerId(),
            dto.sku(),
            dto.title(),
            dto.description(),
            dto.price(),
            dto.currency(),
            dto.categories(),
            dto.active(),
            dto.createdAt(),
            dto.updatedAt(),
            dto.platform(),
            dto.region(),
            dto.minPurchaseQuantity(),
            dto.maxPurchaseQuantity(),
            dto.releaseDate(),
            dto.developer(),
            dto.publisher(),
            dto.metacriticScore(),
            availableStock,
            dto.imageUrls()
        );
    }
}