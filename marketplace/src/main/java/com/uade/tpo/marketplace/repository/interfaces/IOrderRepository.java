package com.uade.tpo.marketplace.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.tpo.marketplace.entity.basic.Order;


public interface IOrderRepository extends JpaRepository<Order, Long>{
    // Optional<Order> getOrderById(Long id);
    // List<Order> getOrdersByBuyerId(Long buyerId);
    // List<Order> getOrders();
    // Order creatOrder(Order order);
    // void deleteOrderById(Long id);
}