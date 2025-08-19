package com.uade.tpo.marketplace.service;

import java.time.Instant;
import java.util.List;

import com.uade.tpo.marketplace.entity.basic.DigitalKey;
import com.uade.tpo.marketplace.entity.enums.KeyStatus;
import com.uade.tpo.marketplace.exceptions.ProductNotFoundException;
import com.uade.tpo.marketplace.repository.interfaces.IDigitalKeyRepository;
import com.uade.tpo.marketplace.repository.interfaces.IProductRepository;

public class DigitalKeyService {
    private IDigitalKeyRepository digitalKeyRepository;
    private IProductRepository productRepository;
    
    public DigitalKeyService(IDigitalKeyRepository digitalKeyRepository, IProductRepository productRepository) {
        this.digitalKeyRepository = digitalKeyRepository;
        this.productRepository = productRepository;
        //AJUSTAR SEGÚN LAS CLASES CONCRETAS DE REPOSITORIOS
    }

    
    public void uploadKeys(Integer productId, List<String> keyCodes, Integer uploaderId, String batchId) throws ProductNotFoundException {

        productRepository.getProductById(productId)
                .orElseThrow(() -> new ProductNotFoundException());

  
        for (String code : keyCodes) {
            DigitalKey k = DigitalKey.builder()
                    .productId(productId)
                    .keyCode(code.trim())
                    .keyMask(mask(code))
                    .status(KeyStatus.AVAILABLE)
                    .createdAt(Instant.now())
                    .build();
            digitalKeyRepository.creatKey(k);
        }
    }


     public Integer countAvailableKeysByProductId(Integer productId) {
        return digitalKeyRepository.countAvailableKeysByProductId(productId);
    }


    public List<DigitalKey> reserveAvailableKeys(Integer productId, int limit) {
        List<DigitalKey> keys = digitalKeyRepository.getAvailableKeysForProduct(productId, limit);
        return keys;
    }


    public void markKeysSold(List<DigitalKey> keys, Integer orderItemId) {
        for (DigitalKey k : keys) {
            k.setStatus(KeyStatus.SOLD);
            k.setSoldAt(Instant.now());
            k.setOrderItemId(orderItemId);
            digitalKeyRepository.creatKey(k);
        }
    }


    private String mask(String key) {
        if (key == null) return null;
        if (key.length() <= 8) return key.replaceAll(".", "*");
        String start = key.substring(0, Math.min(4, key.length()));
        String end = key.substring(Math.max(0, key.length()-4));
        return start + "-****-" + end;
    }

}
