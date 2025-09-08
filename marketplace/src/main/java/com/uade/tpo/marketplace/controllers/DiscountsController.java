package com.uade.tpo.marketplace.controllers;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.entity.dto.create.DiscountCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.DiscountResponseDto;
import com.uade.tpo.marketplace.entity.dto.update.DiscountUpdateDto;
import com.uade.tpo.marketplace.exceptions.DuplicateResourceException;
import com.uade.tpo.marketplace.exceptions.ResourceNotFoundException;
import com.uade.tpo.marketplace.exceptions.UnauthorizedException;
import com.uade.tpo.marketplace.repository.interfaces.IUserRepository;
import com.uade.tpo.marketplace.service.interfaces.IDiscountService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/discounts")
public class DiscountsController {

    @Autowired private IDiscountService discountService;
    @Autowired private IUserRepository userRepository;

    @PostMapping
    public ResponseEntity<DiscountResponseDto> create(@Valid @RequestBody DiscountCreateDto dto,
                                                      Authentication authentication)
            throws DuplicateResourceException, ResourceNotFoundException {
        Long createdByUserId = currentUserId(authentication);
        var out = discountService.createDiscount(dto, createdByUserId);
        return ResponseEntity.created(URI.create("/discounts/" + out.id())).body(out);
    }

    @PutMapping("/{id}")
    public DiscountResponseDto update(@PathVariable Long id,
                                      @Valid @RequestBody DiscountUpdateDto dto,
                                      Authentication authentication)
            throws ResourceNotFoundException, UnauthorizedException, DuplicateResourceException {
        Long requestingUserId = currentUserId(authentication);
        return discountService.updateDiscount(id, dto, requestingUserId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscountResponseDto> getOne(@PathVariable Long id) {
        return discountService.getDiscountById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<DiscountResponseDto> getActiveByCode(@PathVariable String code) {
        return discountService.getActiveDiscountByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/active")
    public Page<DiscountResponseDto> active(Pageable pageable,
                                            @RequestParam(required = false) Boolean onlyActive) {
        return discountService.getActiveDiscounts(pageable, Optional.ofNullable(onlyActive));
    }

    @GetMapping
    public Page<DiscountResponseDto> all(Pageable pageable) {
        return discountService.getAllDiscounts(pageable);
    }

    @GetMapping("/product/{productId}")
    public Page<DiscountResponseDto> activeForProduct(@PathVariable Long productId,
                                                      @RequestParam Integer productQuantity,
                                                      Pageable pageable) {
        return discountService.getActiveDiscountsForProduct(productId, productQuantity, pageable);
    }

    @GetMapping("/buyer/{buyerId}/active-coupons")
    public Page<DiscountResponseDto> allActiveCouponsForBuyer(@PathVariable Long buyerId, Pageable pageable) {
        return discountService.getAllActiveCouponsByTargetBuyerId(buyerId, pageable);
    }

    private Long currentUserId(Authentication authentication) throws ResourceNotFoundException {
        if (authentication == null || authentication.getName() == null) {
            throw new UnauthorizedException("No autenticado");
        }
        final String resolvedEmail = (authentication.getPrincipal() instanceof UserDetails ud)
                ? ud.getUsername()
                : authentication.getName();
        var user = userRepository.findByEmail(resolvedEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email=" + resolvedEmail));
        return user.getId();
    }
}
