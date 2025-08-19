package com.uade.tpo.marketplace.repository.interfaces;

import com.uade.tpo.marketplace.entity.basic.OrderItem;
import java.util.List;
import java.util.Optional;

public interface IOrderItemRepository {
    Optional<OrderItem> getOrderItemById(Integer id);
    List<OrderItem> getOrderItemsByOrderId(Integer orderId);
    OrderItem creaOrderItem(OrderItem item);
}