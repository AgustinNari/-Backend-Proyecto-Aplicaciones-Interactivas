package com.uade.tpo.marketplace.repository.interfaces;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.tpo.marketplace.entity.basic.DigitalKey;
import com.uade.tpo.marketplace.entity.basic.Discount;

public interface IDiscountRepository extends JpaRepository<Discount, Long>{

    List<DigitalKey> getDiscountById(Long productId);

    List<DigitalKey> getDiscountByCode(String code);

    Discount createDiscount(Discount d);


    // Optional<Discount> getDiscountById(Long id);
    // Optional<Discount> getDiscountByCode(String code);
    // List<Discount> getActiveDiscounts();
    // Discount createDiscount(Discount d);
}