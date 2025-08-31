package com.uade.tpo.marketplace.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import com.uade.tpo.marketplace.entity.basic.Product;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long>{
    List<Product> getProducts();
    List<Product> getProductsByCategoryId(Long categoryId);
    Product createProduct(Product product);

    
    // List<Product> getActiveProducts();
    // Optional<Product> getProductById(Long id);
    // void deleteProductById(Long id);
}