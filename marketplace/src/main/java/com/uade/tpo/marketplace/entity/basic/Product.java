package com.uade.tpo.marketplace.entity.basic;


import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private Integer id;
    private Integer sellerId;
    private String sku;
    private String title;
    private String description;
    private BigDecimal price;
    private String currency = "USD";
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