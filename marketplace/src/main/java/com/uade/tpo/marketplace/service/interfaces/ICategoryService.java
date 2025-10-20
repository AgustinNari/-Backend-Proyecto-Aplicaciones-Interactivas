package com.uade.tpo.marketplace.service.interfaces;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.uade.tpo.marketplace.entity.dto.create.CategoryCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.CategoryResponseDto;
import com.uade.tpo.marketplace.entity.dto.update.CategoryUpdateDto;
import com.uade.tpo.marketplace.exceptions.CategoryDuplicateException;
import com.uade.tpo.marketplace.exceptions.ResourceNotFoundException;
import com.uade.tpo.marketplace.exceptions.UnauthorizedException;

public interface  ICategoryService {

        Page<CategoryResponseDto> getAllCategories(PageRequest pageable);

        Optional<CategoryResponseDto> getCategoryById(Long id);

        CategoryResponseDto createCategory(CategoryCreateDto dto) throws CategoryDuplicateException;

        CategoryResponseDto toggleCategoryFeaturedStatus(Long categoryId, boolean featured, Long requestingUserId)
                throws ResourceNotFoundException, UnauthorizedException;

        Page<CategoryResponseDto> getFeaturedCategories(PageRequest pageable);

        CategoryResponseDto updateCategory(Long categoryId, CategoryUpdateDto dto, Long requestingUserId)
                throws ResourceNotFoundException, UnauthorizedException, CategoryDuplicateException;
}
