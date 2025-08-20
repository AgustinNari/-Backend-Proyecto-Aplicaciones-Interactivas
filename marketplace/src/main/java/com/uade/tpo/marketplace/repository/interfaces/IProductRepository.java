package com.uade.tpo.marketplace.repository.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.tpo.marketplace.entity.basic.OrderItem;
import com.uade.tpo.marketplace.entity.basic.Product;


public interface IProductRepository extends JpaRepository<Product, Long>{
    List<Product> getProducts();
    List<Product> getActiveProducts();
    List<Product> getProductsByCategoryId(Long categoryId);
    Optional<Product> getProductById(Long id);
    Product createProduct(Product product);
    void deleteProductById(Long id);
}