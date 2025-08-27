package com.uade.tpo.marketplace.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entity.basic.DigitalKey;



@Repository
public interface IDigitalKeyRepository extends JpaRepository<DigitalKey, Long> {
    // Optional<DigitalKey> getKeyById(Long id);
    // DigitalKey createKey(DigitalKey key);
    // void deleteKeyById(Long id);
    // List<DigitalKey> getKeysByProductId(Long productId);
    // Integer countAvailableKeysByProductId(Long productId);
    // List<DigitalKey> getAvailableKeysForProduct(Long productId, Integer limit);
}