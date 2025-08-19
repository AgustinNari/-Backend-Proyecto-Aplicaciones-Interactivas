package com.uade.tpo.marketplace.entity.basic;

import lombok.*;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    private Integer id;
    private Integer productId;
    private Integer sellerId;
    private Integer buyerId;
    private Integer orderItemId;
    private Integer rating;
    private String title;
    private String comment;
    private boolean visible = true;
    private Instant createdAt = Instant.now();
}
