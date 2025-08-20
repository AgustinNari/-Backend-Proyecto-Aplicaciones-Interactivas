package com.uade.tpo.marketplace.entity.basic;


import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long sellerId;
    private String sku;
    private String title;
    private String description;
    private BigDecimal price;
    private String currency = "USD";

    
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "product_category", // nombre de la tabla intermedia
        joinColumns = @JoinColumn(name = "product_id"), // FK a product
        inverseJoinColumns = @JoinColumn(name = "category_id") // FK a category
    )
    private Set<Category> categories = new HashSet<>();
    private boolean active = true;
    private Instant createdAt = Instant.now();
    private Instant updatedAt;
    private Integer minPurchaseQuantity = 1;
    private Integer maxPurchaseQuantity = 1000;
    private String platform;
    private LocalDate releaseDate;
    private String developer;
    private String publisher;
    private Integer metacriticScore;

}