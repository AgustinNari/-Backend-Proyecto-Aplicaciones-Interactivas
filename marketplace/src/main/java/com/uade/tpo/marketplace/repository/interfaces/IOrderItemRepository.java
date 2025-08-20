package com.uade.tpo.marketplace.repository.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.tpo.marketplace.entity.basic.DigitalKey;
import com.uade.tpo.marketplace.entity.basic.OrderItem;


public interface IOrderItemRepository extends JpaRepository<OrderItem, Long>{
    Optional<OrderItem> getOrderItemById(Long id);
    List<OrderItem> getOrderItemsByOrderId(Long orderId);
    OrderItem creaOrderItem(OrderItem item);
}