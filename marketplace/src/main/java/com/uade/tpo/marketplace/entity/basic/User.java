package com.uade.tpo.marketplace.entity.basic;


import lombok.*;
import java.time.Instant;

import com.uade.tpo.marketplace.entity.enums.Role;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String username;
    private String email;
    private String passwordHash;
    private Role role;
    private String displayName;
    private String phone;
    private boolean active = true;
    private Instant createdAt = Instant.now();
    private Instant lastLogin;
    private BigDecimal sellerBalance = BigDecimal.ZERO;
}