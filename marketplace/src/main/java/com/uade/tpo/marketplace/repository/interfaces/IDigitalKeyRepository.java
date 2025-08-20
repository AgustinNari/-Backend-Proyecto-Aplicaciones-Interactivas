package com.uade.tpo.marketplace.repository.interfaces;

import com.uade.tpo.marketplace.entity.basic.DigitalKey;
import java.util.List;
import java.util.Optional;

public interface IDigitalKeyRepository {
    Optional<DigitalKey> getKeyById(Integer id);
    DigitalKey creatKey(DigitalKey key);
    void deleteKeyById(Integer id);
    List<DigitalKey> getKeysByProductId(Integer productId);
    Integer countAvailableKeysByProductId(Integer productId);
    List<DigitalKey> getAvailableKeysForProduct(Integer productId, Integer limit);
}