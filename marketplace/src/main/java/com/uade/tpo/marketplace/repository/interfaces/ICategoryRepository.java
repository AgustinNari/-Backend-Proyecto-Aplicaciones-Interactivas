package com.uade.tpo.marketplace.repository.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.tpo.marketplace.entity.basic.Category;


public interface ICategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> getCategories();
    Optional<Category> getCategoryById(Integer id);
    Optional<Category> getCategoryByName(String name);
    Category createCategory(Category category);
}