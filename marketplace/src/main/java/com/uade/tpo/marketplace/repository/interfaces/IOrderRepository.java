package com.uade.tpo.marketplace.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.tpo.marketplace.entity.basic.Order;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long>{

    List<Order> getOrderById(Long id);
    Order creatOrder(Order order);

    // List<Order> getOrdersByBuyerId(Long buyerId);
    // List<Order> getOrders();
    // void deleteOrderById(Long id);
}