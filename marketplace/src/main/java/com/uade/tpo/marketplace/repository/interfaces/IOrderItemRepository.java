package com.uade.tpo.marketplace.repository.interfaces;

import java.awt.print.Pageable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entity.basic.OrderItem;

@Repository
public interface IOrderItemRepository extends JpaRepository<OrderItem, Long> {


    List<OrderItem> findByOrderId(Long orderId, Pageable pageable);
    Page<OrderItem> findByProductId(Long productId, Pageable pageable);
    
    @Query("SELECT oi FROM OrderItem oi JOIN FETCH oi.product WHERE oi.order.id = :orderId")
    List<OrderItem> findByOrderIdWithProduct(@Param("orderId") Long orderId);
    
    @Query("SELECT oi FROM OrderItem oi WHERE SIZE(oi.digitalKeys) > 0")
    Page<OrderItem> findItemsWithDigitalKey(Pageable pageable);

    @Query("SELECT DISTINCT oi FROM OrderItem oi LEFT JOIN FETCH oi.digitalKeys WHERE oi.order.id = :orderId")
    List<OrderItem> findByOrderIdWithKeys(@Param("orderId") Long orderId);
    
    @Query("SELECT oi.product.id, COUNT(oi), SUM(oi.quantity), SUM(oi.lineTotal) " +
            "FROM OrderItem oi GROUP BY oi.product.id ORDER BY SUM(oi.quantity) DESC")
    Page<Object[]> findBestSellingProducts(Pageable pageable);
    
    @Query("SELECT oi.product.id, SUM(oi.lineTotal) FROM OrderItem oi GROUP BY oi.product.id")
    Page<Object[]> getTotalSalesByProduct(Pageable pageable);
    
    @Query("SELECT oi FROM OrderItem oi JOIN oi.order o WHERE o.buyer.id = :userId AND o.status = 'COMPLETED'")
    Page<OrderItem> findOrderItemsByUser(@Param("userId") Long userId, Pageable pageable);
}