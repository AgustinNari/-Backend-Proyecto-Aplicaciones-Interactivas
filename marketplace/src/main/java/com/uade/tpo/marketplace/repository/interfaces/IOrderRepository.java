package com.uade.tpo.marketplace.repository.interfaces;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.marketplace.entity.basic.Order;
import com.uade.tpo.marketplace.entity.enums.OrderStatus;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {


    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.buyer.id = :buyerId AND o.status = :status")
    Page<Order> findByBuyerIdAndStatus(Long buyerId, OrderStatus status, Pageable pageable);
    
    Page<Order> findByBuyerId(Long buyerId, Pageable pageable);

    @Query("SELECT DISTINCT o FROM Order o JOIN o.items i WHERE i.product.seller.id = :sellerId")
    Page<Order> findBySellerId(@Param("sellerId") Long sellerId, Pageable pageable);
    
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.items i LEFT JOIN FETCH i.product WHERE o.id = :orderId")
    Optional<Order> findOrderWithItemsAndProducts(@Param("orderId") Long orderId);
    
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.items i LEFT JOIN FETCH i.digitalKeys dk WHERE o.id = :orderId")
    Optional<Order> findOrderWithItemsAndKeys(@Param("orderId") Long orderId);
    
    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.status = :status, o.completedAt = :completedAt WHERE o.id = :orderId")
    int updateOrderStatus(@Param("orderId") Long orderId, @Param("status") OrderStatus status, @Param("completedAt") Instant completedAt);
    
    @Query("SELECT o.buyer.id, COUNT(o), SUM(o.totalAmount) FROM Order o WHERE o.status = 'COMPLETED' GROUP BY o.buyer.id")
    Page<Object[]> getOrderStatisticsByUser(Pageable pageable);
}