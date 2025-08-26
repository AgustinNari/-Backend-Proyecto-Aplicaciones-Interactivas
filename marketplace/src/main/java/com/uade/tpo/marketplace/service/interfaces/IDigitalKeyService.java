package com.uade.tpo.marketplace.service.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.uade.tpo.marketplace.entity.basic.DigitalKey;
import com.uade.tpo.marketplace.entity.dto.response.DigitalKeyResponseDto;
import com.uade.tpo.marketplace.exceptions.DigitalKeyDuplicateException;
import com.uade.tpo.marketplace.exceptions.InsufficientStockException;
import com.uade.tpo.marketplace.exceptions.ResourceNotFoundException;
import com.uade.tpo.marketplace.exceptions.UnauthorizedException;

public interface IDigitalKeyService {
    
    List<DigitalKeyResponseDto> uploadKeys(Long productId, List<String> keyCodes, Long uploaderId)
            throws ResourceNotFoundException, InsufficientStockException, DigitalKeyDuplicateException;

 
    int countAvailableKeysByProductId(Long productId);


    void markKeysSold(List<Long> digitalKeyIds, Long orderItemId) throws ResourceNotFoundException;


    Page<DigitalKeyResponseDto> getKeysByProduct(Long productId, Pageable pageable, Long requestingUserId)
            throws ResourceNotFoundException, UnauthorizedException;
}
