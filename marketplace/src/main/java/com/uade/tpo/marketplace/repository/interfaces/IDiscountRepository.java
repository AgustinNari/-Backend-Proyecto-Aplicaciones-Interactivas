package com.uade.tpo.marketplace.repository.interfaces;


import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.tpo.marketplace.entity.basic.Discount;

public interface IDiscountRepository extends JpaRepository<Discount, Long>{
    // Optional<Discount> getDiscountById(Long id);
    // Optional<Discount> getDiscountByCode(String code);
    // List<Discount> getActiveDiscounts();
    // Discount createDiscount(Discount d);
}