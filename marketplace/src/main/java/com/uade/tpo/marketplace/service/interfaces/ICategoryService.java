package com.uade.tpo.marketplace.service.interfaces;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.uade.tpo.marketplace.entity.dto.create.CategoryCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.CategoryResponseDto;
import com.uade.tpo.marketplace.exceptions.CategoryDuplicateException;

public interface  ICategoryService {

    Page<CategoryResponseDto> getAllCategories(PageRequest pageable);

    Optional<CategoryResponseDto> getCategoryById(Long id);

    CategoryResponseDto createCategory(CategoryCreateDto dto) throws CategoryDuplicateException;

}
