package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.marketplace.entity.basic.Category;
import com.uade.tpo.marketplace.exceptions.CategoryDuplicateException;
import com.uade.tpo.marketplace.repository.interfaces.ICategoryRepository;
import com.uade.tpo.marketplace.service.interfaces.ICategoryService;

@Service
public class CategoryService implements ICategoryService {

    //Aca, deberemos implementar los mismos 3 metodos de la capa de trafico, pero sin las annotations
    
    @Autowired
    private ICategoryRepository categoryRepository;


    
    @Override
    public Page<Category> getCategories(PageRequest pageable) {
        return categoryRepository.findAll(pageable);
    }
    //Quiero que este metodo me traiga todas las categorias de la BD

    //Pero quiero poder indicar un numero para que me devuleva solo alguna categoria tambien

    @Override
    public Optional<Category> getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }


    @Transactional (rollbackFor = Throwable.class)
    @Override
    public Category createCategory(String newCategoryDescription) throws CategoryDuplicateException {
        List<Category> categories = categoryRepository.findByDescription(newCategoryDescription);
        if (!categories.isEmpty()) {
            throw new CategoryDuplicateException();
        }
        return categoryRepository.save(new Category(newCategoryDescription));
    }
    
}
