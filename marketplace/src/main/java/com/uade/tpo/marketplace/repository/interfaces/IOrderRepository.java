package com.uade.tpo.marketplace.repository.interfaces;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
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


    List<Order> findByStatus(OrderStatus status);
    List<Order> findByBuyerIdAndStatus(Long buyerId, OrderStatus status);
    

    List<Order> findByBuyerId(Long buyerId);
    
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.items i LEFT JOIN FETCH i.product WHERE o.id = :orderId")
    Optional<Order> findOrderWithItemsAndProducts(@Param("orderId") Long orderId);
    
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.items WHERE o.buyer.id = :buyerId ORDER BY o.createdAt DESC")
    List<Order> findByBuyerIdWithItems(@Param("buyerId") Long buyerId);
    
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.items i LEFT JOIN FETCH i.digitalKey WHERE o.id = :orderId")
    Optional<Order> findOrderWithItemsAndKeys(@Param("orderId") Long orderId);
    
    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.status = :status, o.completedAt = :completedAt WHERE o.id = :orderId")
    int updateOrderStatus(@Param("orderId") Long orderId, @Param("status") OrderStatus status, @Param("completedAt") Instant completedAt);
    
    @Query("SELECT o.buyer.id, COUNT(o), SUM(o.totalAmount) FROM Order o WHERE o.status = 'COMPLETED' GROUP BY o.buyer.id")
    List<Object[]> getOrderStatisticsByUser();
}