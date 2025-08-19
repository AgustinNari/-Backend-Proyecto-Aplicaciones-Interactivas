package com.uade.tpo.marketplace.repository.interfaces;

import com.uade.tpo.marketplace.entity.basic.Category;

import java.util.List;
import java.util.Optional;

public interface ICategoryRepository {
    List<Category> getCategories();
    Optional<Category> getCategoryById(Integer id);
    Optional<Category> getCategoryByName(String name);
    Category createCategory(Category category);
}