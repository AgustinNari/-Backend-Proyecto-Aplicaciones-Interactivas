package com.uade.tpo.marketplace.entity.basic;


import lombok.*;
import java.time.Instant;

import com.uade.tpo.marketplace.entity.enums.KeyStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DigitalKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private String keyCode;
    private String keyMask;
    private KeyStatus status = KeyStatus.AVAILABLE;
    private Instant reservedAt;
    private Instant soldAt;
    private Long orderItemId;
    private String platform;
    private Instant createdAt = Instant.now();
}