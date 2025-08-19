package com.uade.tpo.marketplace.repository.interfaces;


import com.uade.tpo.marketplace.entity.basic.Discount;
import java.util.Optional;
import java.util.List;

public interface IDiscountRepository {
    Optional<Discount> getDiscountById(Integer id);
    Optional<Discount> getDiscountByCode(String code);
    List<Discount> getActiveDiscounts();
    Discount createDiscount(Discount d);
}