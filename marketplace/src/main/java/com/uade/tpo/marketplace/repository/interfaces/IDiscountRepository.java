package com.uade.tpo.marketplace.repository.interfaces;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.uade.tpo.marketplace.entity.basic.Discount;
import com.uade.tpo.marketplace.entity.enums.DiscountScope;
import com.uade.tpo.marketplace.entity.enums.DiscountType;

@Repository
public interface IDiscountRepository extends JpaRepository<Discount, Long> {

    @Query("SELECT d FROM Discount d WHERE d.startsAt <= :at AND (d.endsAt IS NULL OR d.endsAt >= :at) AND d.active = true")
    List<Discount> findActiveAt(@Param("at") Instant at);
    
    @Query("SELECT d FROM Discount d WHERE d.value >= :minValue")
    List<Discount> findByMinValue(@Param("minValue") BigDecimal minValue);
    
    @Query("SELECT d FROM Discount d WHERE d.startsAt <= CURRENT_TIMESTAMP AND (d.endsAt IS NULL OR d.endsAt >= CURRENT_TIMESTAMP) AND d.active = true")
    List<Discount> findCurrentlyActive();
    

    Optional<Discount> findByCode(String code);
    boolean existsByCode(String code);
    

    List<Discount> findByType(DiscountType type);
    List<Discount> findByScope(DiscountScope scope);
    
    @Query("SELECT d FROM Discount d WHERE d.active = true AND d.scope = 'PRODUCT' AND d.targetProduct.id = :productId " +
           "AND d.startsAt <= CURRENT_TIMESTAMP AND (d.endsAt IS NULL OR d.endsAt >= CURRENT_TIMESTAMP)")
    List<Discount> findActiveProductDiscounts(@Param("productId") Long productId);
    
    @Query("SELECT d FROM Discount d WHERE d.active = true AND d.scope = 'CATEGORY' AND d.targetCategory.id = :categoryId " +
           "AND d.startsAt <= CURRENT_TIMESTAMP AND (d.endsAt IS NULL OR d.endsAt >= CURRENT_TIMESTAMP)")
    List<Discount> findActiveCategoryDiscounts(@Param("categoryId") Long categoryId);
}