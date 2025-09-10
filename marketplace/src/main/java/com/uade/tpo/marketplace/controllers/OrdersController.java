package com.uade.tpo.marketplace.controllers;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.controllers.auth.CurrentUserProvider;
import com.uade.tpo.marketplace.entity.dto.create.OrderCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.OrderItemResponseDto;
import com.uade.tpo.marketplace.entity.dto.response.OrderResponseDto;
import com.uade.tpo.marketplace.exceptions.BadRequestException;
import com.uade.tpo.marketplace.exceptions.InsufficientStockException;
import com.uade.tpo.marketplace.exceptions.ResourceNotFoundException;
import com.uade.tpo.marketplace.exceptions.UnauthorizedException;
import com.uade.tpo.marketplace.service.interfaces.IOrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Autowired private IOrderService orderService;
    @Autowired private CurrentUserProvider currentUserProvider;

    @PostMapping
    public ResponseEntity<OrderResponseDto> create(@Valid @RequestBody OrderCreateDto dto,
                                                   Authentication authentication)
            throws ResourceNotFoundException, InsufficientStockException, BadRequestException {
        Long buyerId = currentUserProvider.getCurrentUserId(authentication);
        OrderResponseDto out = orderService.createOrder(dto, buyerId);
        return ResponseEntity.created(URI.create("/orders/" + out.id())).body(out);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOne(@PathVariable Long id,
                                                   Authentication authentication)
            throws ResourceNotFoundException, UnauthorizedException {
        Long requestingUserId = currentUserProvider.getCurrentUserId(authentication);
        Optional<OrderResponseDto> opt = orderService.getOrderById(id, requestingUserId);
        return opt.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/items")
    public Page<OrderItemResponseDto> items(@PathVariable Long id,
                                            Authentication authentication,
                                            Pageable pageable)
            throws ResourceNotFoundException, UnauthorizedException {
        Long requestingUserId = currentUserProvider.getCurrentUserId(authentication);
        return orderService.getOrderItemsByOrderId(id, requestingUserId, pageable);
    }

    @GetMapping("/my")
    public Page<OrderResponseDto> myOrders(Authentication authentication, Pageable pageable)
            throws ResourceNotFoundException {
        Long buyerId = currentUserProvider.getCurrentUserId(authentication);
        return orderService.getOrdersByBuyer(buyerId, pageable);
    }

    @GetMapping("/seller/{sellerId}")
    public Page<OrderResponseDto> bySeller(@PathVariable Long sellerId, Pageable pageable) {
        return orderService.getOrdersBySeller(sellerId, pageable);
    }

    @GetMapping
    public Page<OrderResponseDto> all(Pageable pageable) {
        return orderService.getAllOrders(pageable);
    }

    @PatchMapping("/{id}/complete")
    public OrderResponseDto complete(@PathVariable Long id, Authentication authentication)
            throws ResourceNotFoundException, UnauthorizedException, BadRequestException {
        Long performedByUserId = currentUserProvider.getCurrentUserId(authentication);
        return orderService.completeOrder(id, performedByUserId);
    }

    @PatchMapping("/{id}/status")
    public OrderResponseDto updateStatus(@PathVariable Long id,
                                         @RequestParam("status") String status,
                                         Authentication authentication)
            throws ResourceNotFoundException, UnauthorizedException, BadRequestException {
        Long performedByUserId = currentUserProvider.getCurrentUserId(authentication);
        return orderService.updateOrderStatus(id, status, performedByUserId);
    }


}
