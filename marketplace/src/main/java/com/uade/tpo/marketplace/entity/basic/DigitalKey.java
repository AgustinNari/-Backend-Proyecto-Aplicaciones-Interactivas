package com.uade.tpo.marketplace.entity.basic;


import lombok.*;
import java.time.Instant;

import com.uade.tpo.marketplace.entity.enums.KeyStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DigitalKey {
    private Integer id;
    private Integer productId;
    private String keyCode;
    private String keyMask;
    private KeyStatus status = KeyStatus.AVAILABLE;
    private Instant reservedAt;
    private Instant soldAt;
    private Integer orderItemId;
    private String platform;
    private Instant createdAt = Instant.now();
}