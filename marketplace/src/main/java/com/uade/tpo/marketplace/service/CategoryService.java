package com.uade.tpo.marketplace.service;

import java.util.ArrayList;
import java.util.Optional;

import com.uade.tpo.marketplace.entity.basic.Category;
import com.uade.tpo.marketplace.exceptions.CategoryDuplicateException;
import com.uade.tpo.marketplace.repository.CategoryRepository;

public class CategoryService {

    //Aca, deberemos implementar los mismos 3 metodos de la capa de trafico, pero sin las annotations
    
    private CategoryRepository categoryRepository;

    
    public CategoryService() {
        this.categoryRepository = new CategoryRepository();
    }
    

    public ArrayList<Category> getCategories() {
        return categoryRepository.getCategories();
    }
    //Quiero que este metodo me traiga todas las categorias de la BD

    //Pero quiero poder indicar un numero para que me devuleva solo alguna categoria tambien

    
    public Optional<Category> getCategoryById(int categoryId) {
        return categoryRepository.getCategoryById(categoryId);
    }


    
    public Category createCategory(String newCategoryDescription) throws CategoryDuplicateException {
        ArrayList<Category> categories = categoryRepository.getCategories();
        if (categories.stream().anyMatch(category -> category.getDescription().equals(newCategoryDescription))) {
            throw new CategoryDuplicateException();
    }
        return categoryRepository.createCategory(newCategoryDescription);
    }
    
}
