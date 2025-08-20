package com.uade.tpo.marketplace.entity.basic;

import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderId;
    


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id2")
    private Order order;




    private Long productId;
    private Long digitalKeyId;
    private Integer quantity = 1;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;
    private Instant createdAt = Instant.now();
}