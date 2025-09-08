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
import com.uade.tpo.marketplace.entity.dto.create.CategoryCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.CategoryResponseDto;
import com.uade.tpo.marketplace.exceptions.CategoryDuplicateException;
import com.uade.tpo.marketplace.service.interfaces.ICategoryService;

import jakarta.validation.Valid;


//El sistema ira recorriendo y viendo las annotations para ver que hacer en cada parte y como tratarlo
@RestController //Esto marca que es un endpoint HTTP, le indica al framework, donde debe ir a buscar, se refiere a que la capa recibe tráfico HTTP, y más específicamente esta clase
@RequestMapping("/categories") //localhost:4002/categories
//Esto de arriba indica que el endpoint es /categories, indicamos el nombre del endpoint
public class CategoriesController {

    @Autowired
    private ICategoryService categoryService; //Usamos esto para no depender de otras capas, para reducir acoplamiento
    //Entonces no vamos a crear instancias de esta interfaz, sino que Spring se encargará de inyectarla

  
    
    @GetMapping //localhost:4002/categories
    public ResponseEntity<Page<CategoryResponseDto>> getCategories(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        PageRequest pr;
        if (page == null || size == null) {
            pr = PageRequest.of(0, Integer.MAX_VALUE);
        } else {
            pr = PageRequest.of(page, size);
        }
        Page<CategoryResponseDto> result = categoryService.getCategories(pr);
        return ResponseEntity.ok(result);
    }
    //Quiero que este metodo me traiga todas las categorias de la BD

    //Pero quiero poder indicar un numero para que me devuleva solo alguna categoria tambien


    @GetMapping("{categoryId}") //localhost:4002/categories/1,2,3 y así
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable Long categoryId) {
        Optional<CategoryResponseDto> result = categoryService.getCategoryById(categoryId);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(@Valid @RequestBody CategoryCreateDto categoryCreateDto)
            throws CategoryDuplicateException {

        CategoryResponseDto created = categoryService.createCategory(categoryCreateDto);
        URI location = URI.create(String.format("categories/", created.id()));
        return ResponseEntity.created(location).body(created);
    }




    
}
