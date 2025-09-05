package com.uade.tpo.marketplace.extra.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.uade.tpo.marketplace.entity.basic.Order;
import com.uade.tpo.marketplace.entity.dto.response.OrderItemResponseDto;
import com.uade.tpo.marketplace.entity.dto.response.OrderResponseDto;

@Component
public class OrderMapper {

    private final OrderItemMapper orderItemMapper;

    public OrderMapper(OrderItemMapper orderItemMapper) {
        this.orderItemMapper = orderItemMapper;
    }

    public OrderResponseDto toResponse(Order order, Boolean includeKeyCodes){
        if (order == null) return null;
        Long buyerId = order.getBuyer() != null ? order.getBuyer().getId() : null;
        List<OrderItemResponseDto> items =
            order.getItems() == null ? java.util.List.of() :
                order.getItems().stream()
                        .map(oi -> orderItemMapper.toResponse(oi, includeKeyCodes))
                        .collect(Collectors.toList());
        return new OrderResponseDto(
            order.getId(), buyerId, order.getSubtotal(), order.getTotalAmount(),
            order.getDiscountAmount(),
            order.getStatus() == null ? null : order.getStatus(),
            items, order.getCreatedAt(), order.getCompletedAt(), order.getNotes()
        );
    }
}