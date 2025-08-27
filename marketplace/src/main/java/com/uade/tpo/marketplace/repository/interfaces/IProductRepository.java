package com.uade.tpo.marketplace.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entity.basic.Product;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long>{
    // List<Product> getProducts();
    // List<Product> getActiveProducts();
    // List<Product> getProductsByCategoryId(Long categoryId);
    // Optional<Product> getProductById(Long id);
    // Product createProduct(Product product);
    // void deleteProductById(Long id);
}