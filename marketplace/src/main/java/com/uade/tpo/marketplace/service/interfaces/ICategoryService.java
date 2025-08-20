package com.uade.tpo.marketplace.service.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.uade.tpo.marketplace.entity.basic.Category;
import com.uade.tpo.marketplace.exceptions.CategoryDuplicateException;

public interface  ICategoryService {

    public List<Category> getCategories();

    public Optional<Category> getCategoryById(Long id);

    public Category createCategory(String newCategoryDescription) throws CategoryDuplicateException;

}
