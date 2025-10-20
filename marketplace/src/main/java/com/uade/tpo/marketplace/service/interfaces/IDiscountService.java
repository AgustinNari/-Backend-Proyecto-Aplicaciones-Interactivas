package com.uade.tpo.marketplace.service.interfaces;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.uade.tpo.marketplace.entity.basic.Discount;
import com.uade.tpo.marketplace.entity.dto.create.DiscountCreateDto;
import com.uade.tpo.marketplace.entity.dto.create.OrderItemCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.CouponValidationResponseDto;
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

        BigDecimal calculateDiscountAmount(Discount discount, OrderItemCreateDto item);

        Page<DiscountResponseDto> getActiveDiscounts(Pageable pageable, Optional<Boolean> onlyActive);

        Page<DiscountResponseDto> getActiveDiscountsForProduct(Long productId, Integer productQuantity, Pageable pageable);

        Page<DiscountResponseDto> getAllDiscounts(Pageable pageable);


        Optional<Discount> validateCouponCodeForOrderItem (String code, Long buyerId, OrderItemCreateDto item, BigDecimal subtotal)
                throws ResourceNotFoundException, BadRequestException;

        Optional<Discount> getHighestValueDiscountForOrderItem (OrderItemCreateDto item)
                throws ResourceNotFoundException;

        Page<DiscountResponseDto> getAllActiveCouponsByTargetBuyerId(Long targetBuyerId, Pageable pageable);

        void markCouponAsUsed(Long discountId, Long targetBuyerId) throws ResourceNotFoundException;

        Optional<DiscountResponseDto> generateNewRandomCoupon(Long targetBuyerId);

        Optional<DiscountResponseDto> getHighestValueDiscountForProduct(Long productId);

        CouponValidationResponseDto validateCouponForOrderItemPreview(String code, Long buyerId, OrderItemCreateDto item);

        CouponValidationResponseDto validateCouponForOrderItemsPreview(String code, Long buyerId, List<OrderItemCreateDto> items);


        Page<DiscountResponseDto> getDiscountsForSellerManagement(Long requestingUserId, Pageable pageable)
                throws ResourceNotFoundException, UnauthorizedException;

        Page<DiscountResponseDto> getDiscountsForAdminManagement(Long requestingUserId, Pageable pageable)
                throws ResourceNotFoundException, UnauthorizedException;

}