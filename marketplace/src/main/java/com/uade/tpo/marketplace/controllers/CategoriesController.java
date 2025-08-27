package com.uade.tpo.marketplace.controllers;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.entity.basic.Category;
import com.uade.tpo.marketplace.entity.dto.response.CategoryResponseDto;
import com.uade.tpo.marketplace.exceptions.CategoryDuplicateException;
import com.uade.tpo.marketplace.service.interfaces.ICategoryService;


//El sistema ira recorriendo y viendo las annotations para ver que hacer en cada parte y como tratarlo
@RestController //Esto marca que es un endpoint HTTP, le indica al framework, donde debe ir a buscar, se refiere a que la capa recibe tráfico HTTP, y más específicamente esta clase
@RequestMapping("categories") //localhost:4002/categories
//Esto de arriba indica que el endpoint es /categories, indicamos el nombre del endpoint
public class CategoriesController {

    @Autowired
    private ICategoryService categoryService; //Usamos esto para no depender de otras capas, para reducir acoplamiento
    //Entonces no vamos a crear instancias de esta interfaz, sino que Spring se encargará de inyectarla

  
    
    @GetMapping //localhost:4002/categories
    public ResponseEntity<Page<Category>> getCategories(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        if (page == null || size == null)
            return ResponseEntity.ok(categoryService.getCategories(PageRequest.of(0, Integer.MAX_VALUE)));
        return ResponseEntity.ok(categoryService.getCategories(PageRequest.of(page, size)));
    }
    //Quiero que este metodo me traiga todas las categorias de la BD

    //Pero quiero poder indicar un numero para que me devuleva solo alguna categoria tambien

    @GetMapping("{categoryId}") //localhost:4002/categories/1,2,3 y así
    public ResponseEntity<Category> getCategoryById(@PathVariable Long categoryId) {
        Optional<Category> result = categoryService.getCategoryById(categoryId);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        }
       
        return ResponseEntity.noContent().build();
    }


    @PostMapping()
    public ResponseEntity<Object> createCategory(@RequestBody CategoryResponseDto categoryRequest)
            throws CategoryDuplicateException {
        Category result = categoryService.createCategory(categoryRequest.getDescription());
      
        return ResponseEntity.created(URI.create("categories/" + result.getId())).body(result);
    }
    
}
