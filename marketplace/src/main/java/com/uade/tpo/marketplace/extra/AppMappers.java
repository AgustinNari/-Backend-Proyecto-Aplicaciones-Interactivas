package com.uade.tpo.marketplace.extra;

import java.util.Set;
import java.util.stream.Collectors;

import com.uade.tpo.marketplace.entity.basic.Category;
import com.uade.tpo.marketplace.entity.basic.Product;
import com.uade.tpo.marketplace.entity.dto.CategoryDto;
import com.uade.tpo.marketplace.entity.dto.ProductDto;

public class AppMappers {
    

   
    public static CategoryDto toCategoryDto(Category c) {
        if (c == null) return null;
        return new CategoryDto(c.getId(), c.getDescription());
    }

    public static Set<CategoryDto> toCategoryDtoSet(Set<Category> cats) {
        return cats == null ? Set.of() : cats.stream().map(AppMappers::toCategoryDto).collect(Collectors.toSet());
    }


    public static ProductDto toProductDto(Product p, Integer availableStock) {
        if (p == null) return null;
        return new ProductDto(
            p.getId(),
            p.getSellerId(),
            p.getSku(),
            p.getTitle(),
            p.getDescription(),
            p.getPrice(),
            p.getCurrency(),
            p.getCategories(),
            p.isActive(),
            availableStock,
            p.getPlatform(),
            p.getReleaseDate(),
            p.getDeveloper(),
            p.getPublisher(),
            p.getMetacriticScore(),
            p.getCreatedAt(),
            p.getUpdatedAt()
        );
    }

}
