package com.uade.tpo.marketplace.service.interfaces;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.uade.tpo.marketplace.entity.basic.Category;
import com.uade.tpo.marketplace.exceptions.CategoryDuplicateException;

public interface  ICategoryService {

    Page<Category> getCategories(PageRequest pageable);

    Optional<Category> getCategoryById(Long id);

    Category createCategory(String newCategoryDescription) throws CategoryDuplicateException;

}
