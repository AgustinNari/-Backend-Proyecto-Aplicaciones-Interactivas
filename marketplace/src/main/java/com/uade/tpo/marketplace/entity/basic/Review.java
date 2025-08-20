package com.uade.tpo.marketplace.entity.basic;

import lombok.*;
import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private Long sellerId;
    private Long buyerId;
    private Long orderItemId;
    private Integer rating;
    private String title;
    private String comment;
    private boolean visible = true;
    private Instant createdAt = Instant.now();
}
