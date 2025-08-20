package com.uade.tpo.marketplace.repository.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.tpo.marketplace.entity.basic.DigitalKey;




public interface IDigitalKeyRepository extends JpaRepository<DigitalKey, Integer> {
    Optional<DigitalKey> getKeyById(Integer id);
    DigitalKey creatKey(DigitalKey key);
    void deleteKeyById(Integer id);
    List<DigitalKey> getKeysByProductId(Integer productId);
    Integer countAvailableKeysByProductId(Integer productId);
    List<DigitalKey> getAvailableKeysForProduct(Integer productId, Integer limit);
}