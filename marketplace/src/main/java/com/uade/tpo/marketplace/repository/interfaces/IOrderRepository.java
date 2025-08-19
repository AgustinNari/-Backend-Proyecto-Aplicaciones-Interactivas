package com.uade.tpo.marketplace.repository.interfaces;

import com.uade.tpo.marketplace.entity.basic.Order;
import java.util.List;
import java.util.Optional;

public interface IOrderRepository {
    Optional<Order> getOrderById(Integer id);
    List<Order> getOrdersByBuyerId(Integer buyerId);
    List<Order> getOrders();
    Order creatOrder(Order order);
    void deleteOrderById(Integer id);
}