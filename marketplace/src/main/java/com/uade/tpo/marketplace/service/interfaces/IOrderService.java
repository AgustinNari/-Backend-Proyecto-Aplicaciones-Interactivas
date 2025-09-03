package com.uade.tpo.marketplace.service.interfaces;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.uade.tpo.marketplace.entity.dto.create.OrderCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.OrderResponseDto;
import com.uade.tpo.marketplace.exceptions.BadRequestException;
import com.uade.tpo.marketplace.exceptions.InsufficientStockException;
import com.uade.tpo.marketplace.exceptions.ResourceNotFoundException;
import com.uade.tpo.marketplace.exceptions.UnauthorizedException;

public interface IOrderService {


        OrderResponseDto createOrder(OrderCreateDto dto, Long buyerId)
                throws ResourceNotFoundException, InsufficientStockException, BadRequestException;

        Optional<OrderResponseDto> getOrderById(Long id, Long requestingUserId) throws ResourceNotFoundException, UnauthorizedException;

        Page<OrderResponseDto> getOrdersByBuyer(Long buyerId, Pageable pageable);

        Page<OrderResponseDto> getOrdersBySeller(Long sellerId, Pageable pageable);

        Page<OrderResponseDto> getAllOrders(Pageable pageable);

        void cancelOrder(Long orderId, Long requestingUserId) throws ResourceNotFoundException, UnauthorizedException, BadRequestException;


        void refundOrder(Long orderId, Long performedByUserId, String reason) throws ResourceNotFoundException, UnauthorizedException;

        OrderResponseDto completeOrder(Long orderId, Long performedByUserId)
                throws ResourceNotFoundException, UnauthorizedException, BadRequestException;

        OrderResponseDto updateOrderStatus(Long orderId, String status, Long performedByUserId)
                throws ResourceNotFoundException, UnauthorizedException, BadRequestException;
}
