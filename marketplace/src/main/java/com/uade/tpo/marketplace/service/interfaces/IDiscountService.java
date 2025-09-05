package com.uade.tpo.marketplace.service.interfaces;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.uade.tpo.marketplace.entity.basic.Discount;
import com.uade.tpo.marketplace.entity.dto.create.DiscountCreateDto;
import com.uade.tpo.marketplace.entity.dto.create.OrderItemCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.DiscountResponseDto;
import com.uade.tpo.marketplace.entity.dto.update.DiscountUpdateDto;
import com.uade.tpo.marketplace.exceptions.BadRequestException;
import com.uade.tpo.marketplace.exceptions.DuplicateResourceException;
import com.uade.tpo.marketplace.exceptions.ResourceNotFoundException;
import com.uade.tpo.marketplace.exceptions.UnauthorizedException;

public interface IDiscountService {

        DiscountResponseDto createDiscount(DiscountCreateDto dto, Long createdByUserId)
                throws DuplicateResourceException, ResourceNotFoundException;

        DiscountResponseDto updateDiscount(Long id, DiscountUpdateDto dto, Long requestingUserId)
                throws ResourceNotFoundException, UnauthorizedException, DuplicateResourceException;

        Optional<DiscountResponseDto> getDiscountById(Long id);

        Optional<DiscountResponseDto> getActiveDiscountByCode(String code);

        BigDecimal calculateDiscountAmount(Discount discount, List<OrderItemCreateDto> items, BigDecimal subtotal);

        void registerDiscountUsage(Long discountId, Long userId, Long orderOrOrderItemId);

        Page<DiscountResponseDto> getActiveDiscounts(Pageable pageable, Optional<Boolean> onlyActive);

        Page<DiscountResponseDto> getAllDiscounts(Pageable pageable);

        DiscountResponseDto validateDiscountCodeForOrder(String code, Long buyerId, List<OrderItemCreateDto> items, BigDecimal subtotal)
                throws ResourceNotFoundException, BadRequestException;

        DiscountResponseDto validateDiscountCodeForOrderItem (String code, Long buyerId, OrderItemCreateDto item, BigDecimal subtotal)
                throws ResourceNotFoundException, BadRequestException;

}