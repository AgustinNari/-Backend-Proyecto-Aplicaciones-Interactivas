package com.uade.tpo.marketplace.entity.basic;


import lombok.*;

import java.time.Instant;

import com.uade.tpo.marketplace.entity.enums.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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

    @OneToMany (mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;
}