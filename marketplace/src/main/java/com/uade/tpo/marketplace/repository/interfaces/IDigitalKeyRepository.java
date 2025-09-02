package com.uade.tpo.marketplace.repository.interfaces;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.uade.tpo.marketplace.entity.basic.DigitalKey;
import com.uade.tpo.marketplace.entity.enums.KeyStatus;

@Repository
public interface IDigitalKeyRepository extends JpaRepository<DigitalKey, Long> {


    List<DigitalKey> findByStatus(KeyStatus status);
    

    @Query("SELECT dk FROM DigitalKey dk WHERE dk.product.id = :productId AND dk.status = 'AVAILABLE'")
    List<DigitalKey> findAvailableByProductId(@Param("productId") Long productId);
    

    boolean existsByKeyCode(String keyCode);
    Optional<DigitalKey> findByKeyCode(String keyCode);
    
    @Query("SELECT COUNT(dk) FROM DigitalKey dk WHERE dk.product.id = :productId AND dk.status = 'AVAILABLE'")
    Long countAvailableByProductId(@Param("productId") Long productId);
    
    @Modifying
    @Transactional
    @Query("UPDATE DigitalKey dk SET dk.status = 'SOLD', dk.soldAt = :soldAt WHERE dk.id = :keyId AND dk.status = 'AVAILABLE'")
    int markAsSold(@Param("keyId") Long keyId, @Param("soldAt") Instant soldAt);
    

    List<DigitalKey> findByProductId(Long productId);
}