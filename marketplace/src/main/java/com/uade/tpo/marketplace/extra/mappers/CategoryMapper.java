package com.uade.tpo.marketplace.extra.mappers;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.uade.tpo.marketplace.entity.basic.Category;
import com.uade.tpo.marketplace.entity.dto.create.CategoryCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.CategoryResponseDto;
import com.uade.tpo.marketplace.entity.dto.update.CategoryUpdateDto;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryCreateDto dto){
        if (dto == null) return null;
        Category c = new Category();
        c.setDescription(dto.description().trim());
        return c;
    }

    public void updateFromDto(CategoryUpdateDto dto, Category entity){
        if (dto == null || entity == null) return;
        if (dto.description() != null) {
            entity.setDescription(dto.description().trim());
        }
    }

    public CategoryResponseDto toResponse(Category category){
        if (category == null) return null;
        int productCount = 0;
        try {
            productCount = category.getProducts() == null ? 0 : category.getProducts().size();
        } catch (Exception e) {
            productCount = 0;
        }
        return new CategoryResponseDto(
            category.getId(),
            category.getDescription(),
            productCount
        );
    }

    public List<CategoryResponseDto> toResponseList(Collection<Category> categories) {
        if (categories == null || categories.isEmpty()) return Collections.emptyList();
        return categories.stream()
                .filter(Objects::nonNull)
                .map(this::toResponse)
                .collect(Collectors.toList()); 
    }

    //TODO: 
    // Esto es un helper como placeholder
    public Category fromId(Long id){
        if (id == null) return null;
        Category c = new Category();
        c.setId(id);
        return c;
    }
}