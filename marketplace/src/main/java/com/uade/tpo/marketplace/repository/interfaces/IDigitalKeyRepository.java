package com.uade.tpo.marketplace.repository.interfaces;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.marketplace.entity.basic.DigitalKey;
import com.uade.tpo.marketplace.entity.basic.Product;
import com.uade.tpo.marketplace.entity.enums.KeyStatus;

import jakarta.persistence.LockModeType;

@Repository
public interface IDigitalKeyRepository extends JpaRepository<DigitalKey, Long> {


    Page<DigitalKey> findByStatus(KeyStatus status, Pageable pageable);

    Page<DigitalKey> findByProductId(Long productId, Pageable pageable);

    boolean existsByKeyCode(String keyCode);

    Optional<DigitalKey> findByKeyCode(String keyCode);

    @Query("SELECT dk FROM DigitalKey dk WHERE dk.product.id = :productId AND dk.status = 'AVAILABLE'")
    Page<DigitalKey> findAvailableByProductId(@Param("productId") Long productId, Pageable pageable);
    
    @Query("SELECT dk FROM DigitalKey dk WHERE dk.product.id = :productId AND dk.status = :status")
    Page<DigitalKey> findByProductIdAndStatus(@Param("productId") Long productId, @Param("status") KeyStatus status, Pageable pageable);
    
    @Query("SELECT COUNT(dk) FROM DigitalKey dk WHERE dk.product.id = :productId AND dk.status = 'AVAILABLE'")
    int countAvailableByProductId(@Param("productId") Long productId);

    @Query("SELECT COUNT(dk) FROM DigitalKey dk WHERE dk.product.id = :productId AND dk.status = :status")
    int countByProductIdAndStatus(@Param("productId") Long productId, @Param("status") KeyStatus status);
    
    @Modifying
    @Transactional
    @Query("UPDATE DigitalKey dk SET dk.status = :toStatus, dk.soldAt = :soldAt WHERE dk.id = :keyId AND dk.status = :fromStatus")
    int markAsSold(@Param("keyId") Long keyId,
                @Param("toStatus") KeyStatus toStatus,
                @Param("fromStatus") KeyStatus fromStatus,
                @Param("soldAt") Instant soldAt);

    @Modifying
    @Transactional
    @Query("UPDATE DigitalKey dk SET dk.status = :toStatus, dk.soldAt = :soldAt WHERE dk.id IN (:keyIds) AND dk.status = :fromStatus")
    int markAsSoldBulk(@Param("keyIds") List<Long> keyIds,
                    @Param("toStatus") KeyStatus toStatus,
                    @Param("fromStatus") KeyStatus fromStatus,
                    @Param("soldAt") Instant soldAt);
    @Modifying
    @Transactional
    @Query(value = "UPDATE digital_keys SET order_item_id = :orderItemId WHERE id IN (:ids)", nativeQuery = true)
    int assignKeysToOrderItem(@Param("orderItemId") Long orderItemId,
                                @Param("ids") List<Long> ids);
    

    //TODO: Importante para reservar claves (Revisar si implementar finalmente o no)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT dk FROM DigitalKey dk WHERE dk.product = :product AND dk.status = :status")
    Page<DigitalKey> findAvailableForUpdateByProductId(@Param("product") Product product,
                                                        @Param("status") KeyStatus status,
                                                        Pageable pageable);


    @Modifying
    @Transactional
    @Query(value = "UPDATE digital_keys SET status = :toStatus, sold_at = :soldAt, order_item_id = :orderItemId " +
                "WHERE id IN (:keyIds) AND status = :fromStatus", nativeQuery = true)
    int markAsSoldAndAssignToOrderItemBulk(@Param("keyIds") List<Long> keyIds,
                                        @Param("toStatus") String toStatus,
                                        @Param("fromStatus") String fromStatus,
                                        @Param("soldAt") Instant soldAt,
                                        @Param("orderItemId") Long orderItemId);

}