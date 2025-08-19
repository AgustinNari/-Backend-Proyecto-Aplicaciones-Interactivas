package com.uade.tpo.marketplace.entity.basic;


import lombok.*;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage {
    private Integer id;
    private Integer productId;
    private String url;
    private String altText;
    private boolean isPrimary = false;
    private Instant createdAt = Instant.now();
}