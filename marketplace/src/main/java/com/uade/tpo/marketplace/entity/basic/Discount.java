package com.uade.tpo.marketplace.entity.basic;


import java.math.BigDecimal;
import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import com.uade.tpo.marketplace.entity.enums.DiscountScope;
import com.uade.tpo.marketplace.entity.enums.DiscountType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "discounts",
       indexes = {@Index(columnList = "code")})
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
  
    @Column(length = 100, unique = true)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private DiscountType type;

    @Column(precision = 12, scale = 2)
    private BigDecimal value;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private DiscountScope scope;

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_product_id")
    private Product targetProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_category_id")
    private Category targetCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_seller_id")
    private User targetSeller;

    @Column(name = "min_quantity")
    private Integer minQuantity;
    

    @Column(name = "starts_at")
    private Instant startsAt;
    
    private Instant endsAt;
    private Integer maxUses;
    private Integer perUserLimit;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "expires_at", nullable = true)
    private Instant expiresAt;
}