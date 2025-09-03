package com.uade.tpo.marketplace.repository.interfaces;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
       Page<Discount> findActiveAt(@Param("at") Instant at, Pageable pageable);
       
       @Query("SELECT d FROM Discount d WHERE d.value >= :minValue")
       Page<Discount> findByMinValue(@Param("minValue") BigDecimal minValue, Pageable pageable);
       
       @Query("SELECT d FROM Discount d WHERE d.startsAt <= CURRENT_TIMESTAMP AND (d.endsAt IS NULL OR d.endsAt >= CURRENT_TIMESTAMP) AND d.active = true")
       Page<Discount> findCurrentlyActive(Pageable pageable);
       
       Page<Discount> findByActiveTrue(Pageable pageable);

       Optional<Discount> findByCode(String code);

       @Query("SELECT d FROM Discount d WHERE d.code = :code AND d.active = true " +
              "AND d.startsAt <= CURRENT_TIMESTAMP AND (d.endsAt IS NULL OR d.endsAt >= CURRENT_TIMESTAMP)")
       Optional<Discount> findByCodeAndActive(String code);

       boolean existsByCode(String code);
       

       Page<Discount> findByType(DiscountType type, Pageable pageable);

       @Query("SELECT d FROM Discount d WHERE d.type = :type AND d.active = :active " +
              "AND d.startsAt <= CURRENT_TIMESTAMP AND (d.endsAt IS NULL OR d.endsAt >= CURRENT_TIMESTAMP)")
       Page<Discount> findByTypeAndActive(DiscountType type, boolean active, Pageable pageable);

       Page<Discount> findByScope(DiscountScope scope, Pageable pageable);


       @Query("SELECT d FROM Discount d WHERE d.active = true AND d.scope = :scope AND d.targetProduct.id = :productId " +
              "AND d.startsAt <= CURRENT_TIMESTAMP AND (d.endsAt IS NULL OR d.endsAt >= CURRENT_TIMESTAMP)")
       Page<Discount> findActiveDiscountsByScopeAndProduct(@Param("scope") DiscountScope scope, @Param("productId") Long productId, Pageable pageable);

       @Query("SELECT d FROM Discount d WHERE d.active = true AND d.scope = :scope AND d.targetCategory.id = :categoryId " +
              "AND d.startsAt <= CURRENT_TIMESTAMP AND (d.endsAt IS NULL OR d.endsAt >= CURRENT_TIMESTAMP)")
       Page<Discount> findActiveDiscountsByScopeAndCategory(@Param("scope") DiscountScope scope, @Param("categoryId") Long categoryId, Pageable pageable);

       @Query("SELECT d FROM Discount d WHERE d.active = true AND d.scope = :scope AND d.targetSeller.id = :sellerId " +
              "AND d.startsAt <= CURRENT_TIMESTAMP AND (d.endsAt IS NULL OR d.endsAt >= CURRENT_TIMESTAMP)")
       Page<Discount> findActiveDiscountsByScopeAndSeller(@Param("scope") DiscountScope scope, @Param("sellerId") Long sellerId, Pageable pageable);
       


       
       @Query("SELECT d FROM Discount d WHERE d.active = true AND d.scope = 'PRODUCT' AND d.targetProduct.id = :productId " +
              "AND d.startsAt <= CURRENT_TIMESTAMP AND (d.endsAt IS NULL OR d.endsAt >= CURRENT_TIMESTAMP)")
       Page<Discount> findActiveProductDiscounts(@Param("productId") Long productId, Pageable pageable);
       
       @Query("SELECT d FROM Discount d WHERE d.active = true AND d.scope = 'CATEGORY' AND d.targetCategory.id = :categoryId " +
              "AND d.startsAt <= CURRENT_TIMESTAMP AND (d.endsAt IS NULL OR d.endsAt >= CURRENT_TIMESTAMP)")
       Page<Discount> findActiveCategoryDiscounts(@Param("categoryId") Long categoryId, Pageable pageable);

       @Query("SELECT d FROM Discount d WHERE d.active = true AND d.scope = 'SELLER' AND d.targetSeller.id = :sellerId " +
              "AND d.startsAt <= CURRENT_TIMESTAMP AND (d.endsAt IS NULL OR d.endsAt >= CURRENT_TIMESTAMP)")
       Page<Discount> findActiveSellerDiscounts(@Param("sellerId") Long sellerId, Pageable pageable);

       
       @Query("SELECT d FROM Discount d WHERE d.targetBuyer.id = :targetBuyerId AND d.active = true " +
              "AND d.startsAt <= CURRENT_TIMESTAMP AND (d.endsAt IS NULL OR d.endsAt >= CURRENT_TIMESTAMP)")
       Page<Discount> findActiveByTargetBuyerId(Long targetBuyerId, Pageable pageable);

       @Query("SELECT d FROM Discount d WHERE d.scope = :scope AND d.targetBuyer.id = :targetBuyerId AND d.active = true " +
              "AND d.startsAt <= CURRENT_TIMESTAMP AND (d.endsAt IS NULL OR d.endsAt >= CURRENT_TIMESTAMP)")
       Page<Discount> findActiveByScopeAndTargetBuyerId(DiscountScope scope, Long targetBuyerId, Pageable pageable);

}