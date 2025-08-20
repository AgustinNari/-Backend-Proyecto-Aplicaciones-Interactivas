package com.uade.tpo.marketplace.repository.interfaces;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.tpo.marketplace.entity.basic.Discount;


public interface IDiscountRepository extends JpaRepository<Discount, Integer> {
    Optional<Discount> getDiscountById(Integer id);
    Optional<Discount> getDiscountByCode(String code);
    List<Discount> getActiveDiscounts();
    Discount createDiscount(Discount d);
}