package com.uade.tpo.marketplace.repository.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.tpo.marketplace.entity.basic.Order;


public interface IOrderRepository extends JpaRepository<Order, Integer> {
    Optional<Order> getOrderById(Integer id);
    List<Order> getOrdersByBuyerId(Integer buyerId);
    List<Order> getOrders();
    Order creatOrder(Order order);
    void deleteOrderById(Integer id);
}