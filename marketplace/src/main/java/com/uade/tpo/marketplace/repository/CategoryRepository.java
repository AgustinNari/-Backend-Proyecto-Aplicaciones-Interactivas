package com.uade.tpo.marketplace.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import com.uade.tpo.marketplace.entity.basic.Category;

public class CategoryRepository {

    private ArrayList<Category> categories = new ArrayList<Category>(
        Arrays.asList(
            Category.builder().id(1).description("Acción").build(),
            Category.builder().id(2).description("Aventura").build(),
            Category.builder().id(3).description("Carreras").build()
        )
    );

    //Aca tenemos que hacer lo mismo de agregar los metodos de las otras capas

    public ArrayList<Category> getCategories() {
        return this.categories;
    }


    
    public Optional<Category> getCategoryById(int categoryId) {
        return this.categories.stream().filter(category -> category.getId() == categoryId).findAny();
    }


    
    public Category createCategory(String newCategoryDescription) {
        Category newCategory = Category.builder().id(this.categories.size()).description(newCategoryDescription).build();
        this.categories.add(newCategory);
        return newCategory;
    }




}
