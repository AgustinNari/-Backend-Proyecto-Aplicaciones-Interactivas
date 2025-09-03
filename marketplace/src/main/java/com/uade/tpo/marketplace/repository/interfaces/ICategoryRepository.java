package com.uade.tpo.marketplace.repository.interfaces;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.uade.tpo.marketplace.entity.basic.Category;
import com.uade.tpo.marketplace.entity.basic.Product;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {


    @Query("SELECT c FROM Category c WHERE c.description = :description")
    Optional<Category> findByDescription(@Param("description") String description);

    @Query("SELECT COUNT(c) > 0 FROM Category c WHERE LOWER(c.description) = LOWER(:description)")
    boolean existsByDescriptionIgnoreCase(String description);

    @Query("SELECT c FROM Category c WHERE LOWER(c.description) LIKE LOWER(CONCAT('%', :term, '%'))")
    Page<Category> findByDescriptionContainingIgnoreCase(@Param("term") String term, Pageable pageable);
    

    @Query("SELECT c FROM Category c WHERE LOWER(c.description) LIKE LOWER(CONCAT('%', :term, '%'))")
    Page<Category> searchByDescriptionContaining(@Param("term") String term, Pageable pageable);
    

    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c.description = :description")
    Page<Product> findProductsByCategoryDescription(@Param("description") String description, Pageable pageable);
    


}