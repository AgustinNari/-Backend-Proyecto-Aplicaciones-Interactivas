package com.uade.tpo.marketplace.service.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.uade.tpo.marketplace.entity.dto.create.OrderCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.AdminStatsExtrasResponseDto;
import com.uade.tpo.marketplace.entity.dto.response.OrderItemResponseDto;
import com.uade.tpo.marketplace.entity.dto.response.OrderKeyResponseDto;
import com.uade.tpo.marketplace.entity.dto.response.OrderResponseDto;
import com.uade.tpo.marketplace.exceptions.BadRequestException;
import com.uade.tpo.marketplace.exceptions.InsufficientStockException;
import com.uade.tpo.marketplace.exceptions.ResourceNotFoundException;
import com.uade.tpo.marketplace.exceptions.UnauthorizedException;

public interface IOrderService {


        OrderResponseDto createOrder(OrderCreateDto dto, Long buyerId)
                throws ResourceNotFoundException, InsufficientStockException, BadRequestException;

        Page<OrderItemResponseDto> getOrderItemsByOrderId(Long orderId, Long requestingUserId, Pageable pageable)
                throws ResourceNotFoundException, UnauthorizedException;


        Optional<OrderResponseDto> getOrderById(Long id, Long requestingUserId) throws ResourceNotFoundException, UnauthorizedException;

        Page<OrderResponseDto> getOrdersByBuyer(Long buyerId, Pageable pageable);

        Page<OrderResponseDto> getOrdersBySeller(Long sellerId, Pageable pageable);

        Page<OrderResponseDto> getAllOrders(Pageable pageable);

        OrderResponseDto completeOrder(Long orderId, Long performedByUserId)
                throws ResourceNotFoundException, UnauthorizedException, BadRequestException;

        OrderResponseDto updateOrderStatus(Long orderId, String status, Long performedByUserId)
                throws ResourceNotFoundException, UnauthorizedException, BadRequestException;

        List<OrderKeyResponseDto> getKeysByOrderId(Long orderId, Long requestingUserId)
                throws ResourceNotFoundException, UnauthorizedException;

        List<OrderKeyResponseDto> getKeysByOrderItemId(Long orderItemId, Long requestingUserId)
                throws ResourceNotFoundException, UnauthorizedException;

        Optional<AdminStatsExtrasResponseDto> getAdminStatsExtras(Long requestingUserId);

        }
