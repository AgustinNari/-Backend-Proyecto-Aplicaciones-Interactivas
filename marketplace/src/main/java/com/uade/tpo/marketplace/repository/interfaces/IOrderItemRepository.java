package com.uade.tpo.marketplace.repository.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.tpo.marketplace.entity.basic.OrderItem;


public interface IOrderItemRepository extends JpaRepository<OrderItem, Integer> {
    Optional<OrderItem> getOrderItemById(Integer id);
    List<OrderItem> getOrderItemsByOrderId(Integer orderId);
    OrderItem creaOrderItem(OrderItem item);
}