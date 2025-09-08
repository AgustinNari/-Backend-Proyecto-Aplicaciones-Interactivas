package com.uade.tpo.marketplace.service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.marketplace.entity.basic.DigitalKey;
import com.uade.tpo.marketplace.entity.basic.Product;
import com.uade.tpo.marketplace.entity.basic.User;
import com.uade.tpo.marketplace.entity.dto.response.DigitalKeyResponseDto;
import com.uade.tpo.marketplace.entity.enums.KeyStatus;
import com.uade.tpo.marketplace.exceptions.DigitalKeyDuplicateException;
import com.uade.tpo.marketplace.exceptions.InsufficientStockException;
import com.uade.tpo.marketplace.exceptions.ResourceNotFoundException;
import com.uade.tpo.marketplace.exceptions.UnauthorizedException;
import com.uade.tpo.marketplace.extra.mappers.DigitalKeyMapper;
import com.uade.tpo.marketplace.repository.interfaces.IDigitalKeyRepository;
import com.uade.tpo.marketplace.repository.interfaces.IProductRepository;
import com.uade.tpo.marketplace.repository.interfaces.IUserRepository;
import com.uade.tpo.marketplace.service.interfaces.IDigitalKeyService;



@Service
public class DigitalKeyService implements IDigitalKeyService {

    @Autowired
    private IDigitalKeyRepository digitalKeyRepository;
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private DigitalKeyMapper digitalKeyMapper;





    @Override
    @Transactional(rollbackFor = Throwable.class)
    public List<DigitalKeyResponseDto> uploadKeys(Long productId, List<String> keyCodes, Long uploaderId)
            throws ResourceNotFoundException, InsufficientStockException, DigitalKeyDuplicateException {

        if (productId == null) throw new ResourceNotFoundException("El id del producto es nulo");
        if (keyCodes == null || keyCodes.isEmpty()) return Collections.emptyList();
    
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado: " + productId));

        User uploader = userRepository.findById(uploaderId)
                .orElseThrow(() -> new UnauthorizedException("Usuario no encontrado: " + uploaderId));

        if (!uploaderId.equals(product.getSeller().getId())) {
            throw new UnauthorizedException("El usuario no es el vendedor del producto, por lo tanto no puede subir claves asociadas.");
        }

        List<String> normalized = keyCodes.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        for (String code : normalized) {
            if (digitalKeyRepository.existsByKeyCode(code)) {
                throw new DigitalKeyDuplicateException("Clave de videojuego duplicada: " + code);
            }
        }

        List<DigitalKey> entities = digitalKeyMapper.toEntitiesFromKeyCodes(productId, normalized);

        List<DigitalKey> saved = digitalKeyRepository.saveAll(entities);

        return saved.stream()
                .map(k -> digitalKeyMapper.toResponse(k, true))
                .collect(Collectors.toList());
    }




    @Override
    public int countAvailableKeysByProductId(Long productId) {
        if (productId == null) return 0;
  
        return digitalKeyRepository.countByProductIdAndStatus(productId, KeyStatus.AVAILABLE);
    }


    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void markKeysSold(List<Long> digitalKeyIds, Long orderItemId) throws ResourceNotFoundException {
        if (digitalKeyIds == null || digitalKeyIds.isEmpty()) return;
        Instant now = Instant.now();

  
        int updated = digitalKeyRepository.markAsSoldAndAssignToOrderItemBulk(
                digitalKeyIds,
                KeyStatus.SOLD.name(),
                KeyStatus.AVAILABLE.name(),
                now,
                orderItemId
        );

        if (updated != digitalKeyIds.size()) {
            throw new ResourceNotFoundException("Algunas claves no fueron actualizadas: " + updated + " de: " + digitalKeyIds.size());
        }
    }

  
    @Override
    public Page<DigitalKeyResponseDto> getAvailableKeysByProduct(Integer keyQuantity, Long productId, Pageable pageable, Long requestingUserId)
            throws ResourceNotFoundException, UnauthorizedException {

        if (productId == null) throw new ResourceNotFoundException("Id del producto nulo");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado: " + productId));

        User requester = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new UnauthorizedException("Usuario no encontrado: " + requestingUserId));

        if (!requester.getId().equals(product.getSeller().getId())) {
            throw new UnauthorizedException("El usuario no es el vendedor del producto, por lo tanto no puede acceder a las claves disponibles.");
        }


    
        Page<DigitalKey> page = digitalKeyRepository.findByProductIdAndStatus(productId, KeyStatus.AVAILABLE, pageable);



        return page.map(k -> digitalKeyMapper.toResponse(k, true));
    }
}