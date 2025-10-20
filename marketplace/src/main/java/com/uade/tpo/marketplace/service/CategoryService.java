package com.uade.tpo.marketplace.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.marketplace.entity.basic.Category;
import com.uade.tpo.marketplace.entity.basic.User;
import com.uade.tpo.marketplace.entity.dto.create.CategoryCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.CategoryResponseDto;
import com.uade.tpo.marketplace.entity.dto.update.CategoryUpdateDto;
import com.uade.tpo.marketplace.entity.enums.Role;
import com.uade.tpo.marketplace.exceptions.BadRequestException;
import com.uade.tpo.marketplace.exceptions.CategoryDuplicateException;
import com.uade.tpo.marketplace.exceptions.CategoryNotFoundException;
import com.uade.tpo.marketplace.exceptions.ResourceNotFoundException;
import com.uade.tpo.marketplace.exceptions.UnauthorizedException;
import com.uade.tpo.marketplace.exceptions.UserNotFoundException;
import com.uade.tpo.marketplace.extra.mappers.CategoryMapper;
import com.uade.tpo.marketplace.repository.interfaces.ICategoryRepository;
import com.uade.tpo.marketplace.repository.interfaces.IUserRepository;
import com.uade.tpo.marketplace.service.interfaces.ICategoryService;

@Service
public class CategoryService implements ICategoryService {

    //Aca, deberemos implementar los mismos 3 metodos de la capa de trafico, pero sin las annotations
    
    @Autowired
    private ICategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private IUserRepository userRepository;

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

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public CategoryResponseDto toggleCategoryFeaturedStatus(Long categoryId, boolean featured, Long requestingUserId)
            throws ResourceNotFoundException, UnauthorizedException {

        if (categoryId == null) throw new BadRequestException("Id de categoría no proporcionado.");
        if (requestingUserId == null) throw new UnauthorizedException("Id de usuario solicitante no proporcionado.");

        User requester = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new UserNotFoundException("Usuario solicitante no encontrado (id=" + requestingUserId + ")."));

        if (requester.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("No tienes permisos para modificar el estado 'featured' de la categoría.");
        }

        int updated = categoryRepository.toggleCategoryFeaturedStatus(categoryId, featured);
        if (updated == 0) {
            throw new CategoryNotFoundException("No se encontró la categoría (id=" + categoryId + ").");
        }

        Category updatedCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("No se encontró la categoría (id=" + categoryId + ")."));

        return categoryMapper.toResponse(updatedCategory);
    }


    @Override
    public Page<CategoryResponseDto> getFeaturedCategories(PageRequest pageable) {
        Page<Category> page = categoryRepository.findByFeaturedTrue(pageable);
        return page.map(categoryMapper::toResponse);
    
}

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public CategoryResponseDto updateCategory(Long categoryId, CategoryUpdateDto dto, Long requestingUserId)
            throws ResourceNotFoundException, UnauthorizedException, CategoryDuplicateException {

        if (categoryId == null) {
            throw new BadRequestException("Id de categoría no proporcionado.");
        }
        if (dto == null || dto.description() == null || dto.description().isBlank()) {
            throw new BadRequestException("La descripción de la categoría no puede estar vacía.");
        }
        if (requestingUserId == null) {
            throw new UnauthorizedException("Id de usuario solicitante no proporcionado.");
        }

        User requester = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new UserNotFoundException("Usuario solicitante no encontrado (id=" + requestingUserId + ")."));

        if (requester.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("No tienes permisos para modificar la descripción de la categoría.");
        }

        Category existing = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("No se encontró la categoría (id=" + categoryId + ")."));

        String newDescTrimmed = dto.description().trim();

        Optional<Category> sameDesc = categoryRepository.findByDescription(newDescTrimmed);

        if (sameDesc.isPresent() && !Objects.equals(sameDesc.get().getId(), categoryId)) {
            throw new CategoryDuplicateException("Ya existe una categoría con la descripción: " + newDescTrimmed);
        }

        existing.setDescription(newDescTrimmed);
        Category saved = categoryRepository.save(existing);

        return categoryMapper.toResponse(saved);
    }

}