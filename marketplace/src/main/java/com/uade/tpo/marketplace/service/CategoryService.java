package com.uade.tpo.marketplace.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.marketplace.entity.basic.Category;
import com.uade.tpo.marketplace.entity.dto.create.CategoryCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.CategoryResponseDto;
import com.uade.tpo.marketplace.exceptions.CategoryDuplicateException;
import com.uade.tpo.marketplace.extra.mappers.CategoryMapper;
import com.uade.tpo.marketplace.repository.interfaces.ICategoryRepository;
import com.uade.tpo.marketplace.service.interfaces.ICategoryService;

@Service
public class CategoryService implements ICategoryService {

    //Aca, deberemos implementar los mismos 3 metodos de la capa de trafico, pero sin las annotations
    
    @Autowired
    private ICategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Page<CategoryResponseDto> getAllCategories(PageRequest pageable) {
    Page<Category> page = categoryRepository.findAll(pageable);
    return page.map(categoryMapper::toResponse);
}
    //Quiero que este metodo me traiga todas las categorias de la BD

    //Pero quiero poder indicar un numero para que me devuleva solo alguna categoria tambien

    @Override
    public Optional<CategoryResponseDto> getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).map(categoryMapper::toResponse);
}


    @Transactional (rollbackFor = Throwable.class)
    @Override
    public CategoryResponseDto createCategory(CategoryCreateDto dto) throws CategoryDuplicateException {
        if (dto == null || dto.description() == null || dto.description().isBlank()) {
            throw new IllegalArgumentException("La descripción de la categoría no puede estar vacía.");
        }

        if (categoryRepository.existsByDescriptionIgnoreCase(dto.description().trim())) {
            throw new CategoryDuplicateException("Ya existe una categoría con la descripción: " + dto.description().trim());
        }

        Category entity = categoryMapper.toEntity(dto);
        Category saved = categoryRepository.save(entity);
        return categoryMapper.toResponse(saved);
    }
    
}
