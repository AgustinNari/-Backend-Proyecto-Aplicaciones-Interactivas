package com.uade.tpo.marketplace.repository;

import java.util.ArrayList;
import java.util.Arrays;


import com.uade.tpo.marketplace.entity.Category;

public class CategoryRepository {

    private ArrayList<Category> categories = new ArrayList<Category>(
        Arrays.asList(
            Category.builder().id(1).description("Videojuegos").build(),
            Category.builder().id(2).description("Tarjetas de Regalo").build(),
            Category.builder().id(3).description("Monedas Virtuales").build()
        )
    );

    //Aca tenemos que hacer lo mismo de agregar los metodos de las otras capas

    public ArrayList<Category> getCategories() {
        return this.categories;
    }


    
    public Category getCategoryById(int categoryId) {
        return null;
    }


    
    public String createCategory(String entity) {

        
        return null;
    }




}
