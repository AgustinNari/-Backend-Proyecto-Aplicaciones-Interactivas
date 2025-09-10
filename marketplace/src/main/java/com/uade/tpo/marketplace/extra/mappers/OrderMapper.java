package com.uade.tpo.marketplace.extra.mappers;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.uade.tpo.marketplace.entity.basic.Order;
import com.uade.tpo.marketplace.entity.basic.OrderItem;
import com.uade.tpo.marketplace.entity.basic.User;
import com.uade.tpo.marketplace.entity.dto.create.OrderCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.OrderItemResponseDto;
import com.uade.tpo.marketplace.entity.dto.response.OrderResponseDto;
import com.uade.tpo.marketplace.entity.dto.response.OrderSummaryDto;
import com.uade.tpo.marketplace.entity.dto.update.OrderUpdateDto;

@Component
public class OrderMapper {

    private final OrderItemMapper orderItemMapper;

    public OrderMapper() {
        this.orderItemMapper = new OrderItemMapper();
    }
  public OrderResponseDto toResponse(Order order, Boolean includeKeyCodes){
        if (order == null) return null;

        Long buyerId = order.getBuyer() != null ? safeGetId(order.getBuyer()) : null;

        List<OrderItemResponseDto> items = order.getItems() == null ? Collections.emptyList() :
            order.getItems().stream()
                .filter(Objects::nonNull)
                .map(oi -> orderItemMapper.toResponse(oi, Boolean.TRUE.equals(includeKeyCodes)))
                .collect(Collectors.toList());

        return new OrderResponseDto(
            order.getId(),
            buyerId,
            order.getSubtotal(),
            order.getTotalAmount(),
            order.getDiscountAmount(),
            order.getStatus(),
            items,
            order.getCreatedAt(),
            order.getCompletedAt(),
            order.getNotes()
        );
    }

    public List<OrderResponseDto> toResponseList(Collection<Order> orders, boolean includeKeyCodes) {
        if (orders == null || orders.isEmpty()) return Collections.emptyList();
        return orders.stream()
                .filter(Objects::nonNull)
                .map(o -> toResponse(o, includeKeyCodes))
                .collect(Collectors.toList());
    }

    public Order toEntityFromCreate(OrderCreateDto dto, Long buyerId) {
        if (dto == null) return null;
        Order order = new Order();
        if (buyerId != null) {
            User buyer = new User();
            buyer.setId(buyerId);
            order.setBuyer(buyer);
        }

        Set<OrderItem> items = dto.items() == null || dto.items().isEmpty()
                ? Collections.emptySet()
                : dto.items().stream()
                    .filter(Objects::nonNull)
                    .map(itemDto -> {
                        BigDecimal unitPrice = null;
                        OrderItem oi = orderItemMapper.toEntityFromCreate(itemDto, unitPrice);
                        return oi;
                    }).collect(Collectors.toSet());

        order.setItems(items);
        order.setNotes(dto.notes());
        order.setSubtotal(BigDecimal.ZERO);
        order.setTotalAmount(BigDecimal.ZERO);
        order.setDiscountAmount(BigDecimal.ZERO);
        return order;
    }

    public OrderSummaryDto computeSummary(Order order) {
        if (order == null) return new OrderSummaryDto(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);

        BigDecimal subtotal = order.getItems() == null ? BigDecimal.ZERO :
            order.getItems().stream()
                 .filter(Objects::nonNull)
                 .map(oi -> oi.getLineTotal() != null ? oi.getLineTotal() : BigDecimal.ZERO)
                 .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal discount = order.getDiscountAmount() != null ? order.getDiscountAmount() : BigDecimal.ZERO;
        BigDecimal total = order.getTotalAmount() != null ? order.getTotalAmount() : subtotal.subtract(discount);

        return new OrderSummaryDto(subtotal, discount, total);
    }

    public void updateFromDto(OrderUpdateDto dto, Order entity) {
        if (dto == null || entity == null) return;
        if (dto.status() != null) entity.setStatus(dto.status());
        if (dto.notes() != null) entity.setNotes(dto.notes());
    }
    private Long safeGetId(User u) {
        try { return u.getId(); } catch (Exception e) { return null; }
    }



}