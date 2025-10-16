package com.uade.tpo.marketplace.extra.mappers;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.uade.tpo.marketplace.entity.basic.Category;
import com.uade.tpo.marketplace.entity.basic.Product;
import com.uade.tpo.marketplace.entity.basic.ProductImage;
import com.uade.tpo.marketplace.entity.basic.User;
import com.uade.tpo.marketplace.entity.dto.create.ProductCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.CategoryResponseDto;
import com.uade.tpo.marketplace.entity.dto.response.ProductImageResponseDto;
import com.uade.tpo.marketplace.entity.dto.response.ProductResponseDto;
import com.uade.tpo.marketplace.entity.dto.update.ProductUpdateDto;

@Component
public class ProductMapper {

    private final CategoryMapper categoryMapper;
    private final ProductImageMapper productImageMapper;

    public ProductMapper() {
        this.categoryMapper = new CategoryMapper();
        this.productImageMapper =  new ProductImageMapper();
    }

    public Product toEntity(ProductCreateDto dto, Long sellerId){
        if (dto == null) return null;
        Product p = new Product();

        if (sellerId != null) {
            User seller = new User();
            seller.setId(sellerId);
            p.setSeller(seller);
        }

        if (dto.sku() != null) p.setSku(dto.sku().trim());
        p.setTitle(dto.title() != null ? dto.title().trim() : null);
        p.setDescription(dto.description());
        p.setPrice(dto.price() != null ? dto.price() : BigDecimal.ZERO);
        if (dto.currency() != null) p.setCurrency(dto.currency());
        p.setPlatform(dto.platform());
        p.setRegion(dto.region());
        p.setMinPurchaseQuantity(dto.minPurchaseQuantity() == null ? 1 : dto.minPurchaseQuantity());
        p.setMaxPurchaseQuantity(dto.maxPurchaseQuantity() == null ? 1000 : dto.maxPurchaseQuantity());
        p.setReleaseDate(dto.releaseDate());
        p.setDeveloper(dto.developer());
        p.setPublisher(dto.publisher());
        p.setMetacriticScore(dto.metacriticScore());
        p.setActive(true);

        if (dto.categoryIds() != null && !dto.categoryIds().isEmpty()) {
            Set<Category> cats = dto.categoryIds().stream()
                .filter(Objects::nonNull)
                .map(id -> {
                    Category c = new Category();
                    c.setId(id);
                    return c;
                })
                .collect(Collectors.toSet());
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
            Set<Category> cats = dto.categoryIds().stream()
                .filter(Objects::nonNull)
                .map(id -> {
                    Category c = new Category();
                    c.setId(id);
                    return c;
                }).collect(Collectors.toSet());
            entity.setCategories(cats);
        }
        if (dto.active() != null) entity.setActive(dto.active());
    }

 
    public ProductResponseDto toResponse(Product product, String sellerDisplayName){
        if (product == null) return null;

        Set<CategoryResponseDto> categoryDtos = product.getCategories() == null
            ? Collections.emptySet()
            : product.getCategories().stream()
                .filter(Objects::nonNull)
                .map(categoryMapper::toResponse)
                .collect(Collectors.toSet());

        List<String> imageFiles = product.getImages() == null
            ? Collections.emptyList()
            : product.getImages().stream()
                .filter(Objects::nonNull)
                .map((ProductImage img) -> productImageMapper.toResponse(img))
                .filter(Objects::nonNull)
                .map(ProductImageResponseDto::file)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Long sellerId = product.getSeller() != null ? safeGetId(product.getSeller()) : null;

        Integer availableStock = null; // No calculado en este método

        return new ProductResponseDto(
            product.getId(),
            sellerId,
            sellerDisplayName,
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
            availableStock,
            imageFiles,
            product.isFeatured()
        );
    }

    //Se ingresa la cantidad de stock disponible de manera externa
    public ProductResponseDto toResponse(Product product, int availableStock, String sellerDisplayName) {
        ProductResponseDto base = toResponse(product, sellerDisplayName);
        if (base == null) return null;
        return new ProductResponseDto(
            base.id(),
            base.sellerId(),
            base.sellerDisplayName(),
            base.sku(),
            base.title(),
            base.description(),
            base.price(),
            base.currency(),
            base.categories(),
            base.active(),
            base.createdAt(),
            base.updatedAt(),
            base.platform(),
            base.region(),
            base.minPurchaseQuantity(),
            base.maxPurchaseQuantity(),
            base.releaseDate(),
            base.developer(),
            base.publisher(),
            base.metacriticScore(),
            availableStock,
            base.imageUrls(),
            base.featured()
        );
    }



    public List<ProductResponseDto> toResponseList(Collection<Product> products,
                                                   java.util.Map<Long, Integer> stocksByProductId,
                                                   java.util.Map<Long, String> sellerNamesBySellerId) {
        if (products == null || products.isEmpty()) return Collections.emptyList();

        return products.stream()
                .filter(Objects::nonNull)
                .map(p -> {
                    String sellerName = null;
                    if (sellerNamesBySellerId != null && p.getSeller()!=null) {
                        sellerName = sellerNamesBySellerId.get(p.getSeller().getId());
                    }
                    Integer stock = null;
                    if (stocksByProductId != null) {
                        stock = stocksByProductId.get(p.getId());
                    }
                    if (stock != null) {
                        return toResponse(p, stock, sellerName);
                    } else {
                        return toResponse(p, sellerName);
                    }
                })
                .collect(Collectors.toList());
    }

    public List<ProductResponseDto> toResponseList(Collection<Product> products) {
        return toResponseList(products, null, null);
    }

    //Sirve si se configura con JPA para que traiga las digitalKeys asociadas, en FetchType.EAGER
    public int computeAvailableStock(Product product) {
        if (product == null || product.getDigitalKeys() == null) return 0;
        return (int) product.getDigitalKeys().stream()
                .filter(Objects::nonNull)
                .filter(dk -> dk.getStatus() != null && dk.getStatus().name().equals("AVAILABLE"))
                .count();
    }

    public Product fromId(Long id){
        if (id == null) return null;
        Product p = new Product();
        p.setId(id);
        return p;
    }

    private Long safeGetId(User u) {
        try { return u.getId(); } catch (Exception e) { return null; }
    }
}