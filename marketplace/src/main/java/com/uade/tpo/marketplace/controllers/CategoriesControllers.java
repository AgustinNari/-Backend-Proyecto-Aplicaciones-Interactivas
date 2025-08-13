package com.uade.tpo.marketplace.controllers;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.entity.Category;
import com.uade.tpo.marketplace.service.CategoryService;


//El sisttema ira recorriendo y viendo las annotations para ver que hacer en cada parte y como tratarlo
@RestController //Esto marca que es un endpoint HTTP, le indica al framework, donde debe ir a buscar, se refiere a que la capa recibe tráfico HTTP, y más específicamente esta clase
@RequestMapping("categories") //localhost:4002/categories
//Esto de arriba indica que el endpoint es /categories, indicamos el nombre del endpoint
public class CategoriesControllers {

    
    @GetMapping //localhost:4002/categories
    public ArrayList<Category> getCategories() {
        CategoryService categoryService = new CategoryService();
        return categoryService.getCategories();
    }
    //Quiero que este metodo me traiga todas las categorias de la BD

    //Pero quiero poder indicar un numero para que me devuleva solo alguna categoria tambien

    @GetMapping("{categoryId}") //localhost:4002/categories/1,2,3 y así
    public Category getCategoryById(@PathVariable int categoryId) {
        CategoryService categoryService = new CategoryService();
        return categoryService.getCategoryById(categoryId);
    }


    @PostMapping()
    public String createCategory(@RequestBody String categoryId) {
        CategoryService categoryService = new CategoryService();
        return categoryService.createCategory(categoryId);
    }
    
}
