package com.uade.tpo.marketplace.repository.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entity.basic.Category;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
    
    // List<Category> getCategories();
    // Optional<Category> getCategoryById(Long id);
    // Optional<Category> getCategoryByName(String name);
    // Category createCategory(Category category);



    @Query ("SELECT c FROM Category c WHERE c.description = :description")
    List<Category> findByDescription(@Param  ("description") String description);
}