package com.uade.tpo.marketplace.repository.interfaces;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.uade.tpo.marketplace.entity.basic.OrderItem;

@Repository
public interface IOrderItemRepository extends JpaRepository<OrderItem, Long> {


    List<OrderItem> findByOrderId(Long orderId);
    List<OrderItem> findByProductId(Long productId);
    
    @Query("SELECT oi FROM OrderItem oi JOIN FETCH oi.product WHERE oi.order.id = :orderId")
    List<OrderItem> findByOrderIdWithProduct(@Param("orderId") Long orderId);
    
    @Query("SELECT oi FROM OrderItem oi WHERE oi.digitalKey IS NOT NULL")
    List<OrderItem> findItemsWithDigitalKey();
    
    @Query("SELECT oi.product.id, COUNT(oi), SUM(oi.quantity), SUM(oi.lineTotal) " +
           "FROM OrderItem oi GROUP BY oi.product.id ORDER BY SUM(oi.quantity) DESC")
    List<Object[]> findBestSellingProducts();
    
    @Query("SELECT oi.product.id, SUM(oi.lineTotal) FROM OrderItem oi GROUP BY oi.product.id")
    List<Object[]> getTotalSalesByProduct();
    
    @Query("SELECT oi FROM OrderItem oi JOIN oi.order o WHERE o.buyer.id = :userId AND o.status = 'PENDING'")
    List<OrderItem> findCartItemsByUser(@Param("userId") Long userId);
    
    @Query("SELECT oi FROM OrderItem oi JOIN oi.order o WHERE o.status = 'PROCESSING'")
    List<OrderItem> findItemsToProcess();
}