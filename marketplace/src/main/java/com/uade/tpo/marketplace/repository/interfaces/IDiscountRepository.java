package com.uade.tpo.marketplace.repository.interfaces;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entity.basic.Discount;

@Repository
public interface IDiscountRepository extends JpaRepository<Discount, Long>{
    // Optional<Discount> getDiscountById(Long id);
    // Optional<Discount> getDiscountByCode(String code);
    // List<Discount> getActiveDiscounts();
    // Discount createDiscount(Discount d);
}