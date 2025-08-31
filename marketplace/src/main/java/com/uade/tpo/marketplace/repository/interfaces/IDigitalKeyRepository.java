package com.uade.tpo.marketplace.repository.interfaces;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.tpo.marketplace.entity.basic.DigitalKey;


@Repository
public interface IDigitalKeyRepository extends JpaRepository<DigitalKey, Long> {

    List<DigitalKey> findByProductId(Long productId);

    List<DigitalKey> findByUserId(Long userId);

    boolean existsByKeyCode(String keyCode);

    DigitalKey createKey(DigitalKey key);




    // Optional<DigitalKey> getKeyById(Long id);
    // DigitalKey createKey(DigitalKey key);
    // void deleteKeyById(Long id);
    // List<DigitalKey> getKeysByProductId(Long productId);
    // Integer countAvailableKeysByProductId(Long productId);
    // List<DigitalKey> getAvailableKeysForProduct(Long productId, Integer limit);
}