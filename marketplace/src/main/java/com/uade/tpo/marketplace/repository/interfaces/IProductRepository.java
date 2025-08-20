package com.uade.tpo.marketplace.repository.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.tpo.marketplace.entity.basic.Product;


public interface IProductRepository extends JpaRepository<Product, Integer> {
    List<Product> getProducts();
    List<Product> getActiveProducts();
    List<Product> getProductsByCategoryId(Integer categoryId);
    Optional<Product> getProductById(Integer id);
    Product createProduct(Product product);
    void deleteProductById(Integer id);
}