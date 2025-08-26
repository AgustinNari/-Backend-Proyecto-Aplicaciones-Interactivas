package com.uade.tpo.marketplace.entity.basic;


import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import com.uade.tpo.marketplace.entity.enums.KeyStatus;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "digital_keys",
       indexes = {@Index(columnList = "product_id"), @Index(columnList = "status")})
public class DigitalKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "key_code", nullable = false, length = 512, unique = true)
    private String keyCode;

    @Column(name = "key_mask", length = 128)
    private String keyMask;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private KeyStatus status = KeyStatus.AVAILABLE;

    @Column(name = "sold_at")
    private Instant soldAt;

    @OneToOne(mappedBy = "digitalKey", fetch = FetchType.LAZY)
    private OrderItem orderItem;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    

}