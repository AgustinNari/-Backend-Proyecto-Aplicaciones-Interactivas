package com.uade.tpo.marketplace.repository.interfaces;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.uade.tpo.marketplace.entity.basic.Category;
import com.uade.tpo.marketplace.entity.basic.Product;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {


    @Query("SELECT c FROM Category c WHERE c.description = :description")
    List<Category> findByDescription(@Param("description") String description);
    

    boolean existsByDescription(String description);
    

    @Query("SELECT c FROM Category c WHERE LOWER(c.description) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Category> searchByDescriptionContaining(@Param("term") String term);
    

    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c.description = :description")
    List<Product> findProductsByCategoryDescription(@Param("description") String description);
    


}