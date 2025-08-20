package com.uade.tpo.marketplace.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entity.basic.Discount;
import com.uade.tpo.marketplace.entity.dto.DiscountCreateDto;
import com.uade.tpo.marketplace.entity.enums.DiscountScope;
import com.uade.tpo.marketplace.entity.enums.DiscountType;
import com.uade.tpo.marketplace.repository.interfaces.IDiscountRepository;

@Service
public class DiscountService {
    
    private  IDiscountRepository discountRepository;

    public DiscountService(IDiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
        //AJUSTAR SEGÚN LAS CLASES CONCRETAS DE REPOSITORIOS
    }
    

    public Optional<Discount> getValidDiscountByCode(String code) {
        if (code == null || code.isBlank()) return Optional.empty();
        Optional<Discount> d = discountRepository.getDiscountByCode(code);
        if (d.isEmpty()) return Optional.empty();
        Discount disc = d.get();
        Instant now = Instant.now();
        if (!disc.isActive()) return Optional.empty();
        if (disc.getStartsAt() != null && disc.getStartsAt().isAfter(now)) return Optional.empty();
        if (disc.getEndsAt() != null && disc.getEndsAt().isBefore(now)) return Optional.empty();
        return Optional.of(disc);
    }


    public BigDecimal calculateOrderLevelDiscountAmount(Discount discount, BigDecimal subtotal) {
        if (discount == null) return BigDecimal.ZERO;
        if (discount.getType() == null) return BigDecimal.ZERO;
        return switch (discount.getType()) {
            case PERCENT -> subtotal.multiply(discount.getValue()).divide(BigDecimal.valueOf(100));
            case FIXED -> discount.getValue().min(subtotal);
            default -> BigDecimal.ZERO;
        };
    }


    public Discount createDiscount(DiscountCreateDto dto) {
        Discount d = Discount.builder()
                .code(dto.code())
                .type(dto.type() == null ? null : DiscountType.valueOf(dto.type()))
                .value(dto.value())
                .scope(dto.scope() == null ? null : DiscountScope.valueOf(dto.scope()))
                .targetId(dto.targetId())
                .minQuantity(dto.minQuantity())
                .startsAt(dto.startsAt())
                .endsAt(dto.endsAt())
                .maxUses(dto.maxUses())
                .perUserLimit(dto.perUserLimit())
                .active(true)
                .build();
        return discountRepository.createDiscount(d);
    }

}
