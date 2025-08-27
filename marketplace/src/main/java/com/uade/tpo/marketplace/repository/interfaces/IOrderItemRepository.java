package com.uade.tpo.marketplace.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entity.basic.OrderItem;

@Repository
public interface IOrderItemRepository extends JpaRepository<OrderItem, Long>{
    // Optional<OrderItem> getOrderItemById(Long id);
    // List<OrderItem> getOrderItemsByOrderId(Long orderId);
    // OrderItem creaOrderItem(OrderItem item);
}