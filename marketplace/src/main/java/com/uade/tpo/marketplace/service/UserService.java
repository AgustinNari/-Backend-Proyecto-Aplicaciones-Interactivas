package com.uade.tpo.marketplace.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entity.basic.User;
import com.uade.tpo.marketplace.entity.dto.response.SellerResponseDto;
import com.uade.tpo.marketplace.entity.dto.response.UserResponseDto;
import com.uade.tpo.marketplace.entity.dto.update.UserUpdateDto;
import com.uade.tpo.marketplace.entity.enums.Role;
import com.uade.tpo.marketplace.exceptions.DuplicateResourceException;
import com.uade.tpo.marketplace.exceptions.ResourceNotFoundException;
import com.uade.tpo.marketplace.exceptions.UnauthorizedException;
import com.uade.tpo.marketplace.exceptions.UserNotFoundException;
import com.uade.tpo.marketplace.extra.mappers.UserMapper;
import com.uade.tpo.marketplace.repository.interfaces.IReviewRepository;
import com.uade.tpo.marketplace.repository.interfaces.IUserRepository;
import com.uade.tpo.marketplace.service.interfaces.IUserService;

import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IReviewRepository reviewRepository;

    private final UserMapper userMapper;

    public UserService() {
        this.userMapper = new UserMapper();
    }





    
   @Override
    public Optional<UserResponseDto> getUserById(Long id) {
        if (id == null) return Optional.empty();
        return userRepository.findById(id).map(userMapper::toResponse);
    }

    @Override
    public Optional<UserResponseDto> getUserByMail(String email) {
        if (email == null || email.isBlank()) return Optional.empty();
        return userRepository.findByEmailIgnoreCase(email).map(userMapper::toResponse);
    }


    @Override
    @Transactional(rollbackFor = Throwable.class)
    public UserResponseDto updateUser(Long id, UserUpdateDto dto, Long requestingUserId)
            throws ResourceNotFoundException, UnauthorizedException, DuplicateResourceException {

        if (id == null) throw new ResourceNotFoundException("Id de usuario no proporcionado.");
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado (id=" + id + ")."));


        if (requestingUserId == null || (!requestingUserId.equals(id))) {
            throw new UnauthorizedException("No tienes permiso para realizar esta acción.");
        }


        if (dto.displayName() != null && !dto.displayName().isBlank()) {
            String newDisplay = dto.displayName().trim();
            String currentDisplay = existing.getDisplayName();
            if (currentDisplay == null || !currentDisplay.equalsIgnoreCase(newDisplay)) {
                boolean exists = userRepository.existsByDisplayNameIgnoreCase(newDisplay);
                if (exists) {
                    throw new DuplicateResourceException("Ya existe otro usuario con ese nombre de usuario (displayName).");
                }
            }
        }

        userMapper.updateFromDto(dto, existing);

        User saved = userRepository.save(existing);
        return userMapper.toResponse(saved);
    }

    @Override
    public Page<UserResponseDto> getUsers(Pageable pageable, Optional<Role> roleFilter) {
        if (roleFilter != null && roleFilter.isPresent()) {
            Page<User> page = userRepository.findByRole(roleFilter.get(), pageable);
            List<UserResponseDto> dtos = page.getContent().stream().map(userMapper::toResponse).collect(Collectors.toList());
            return new PageImpl<>(dtos, pageable, page.getTotalElements());
        } else {
            Page<User> page = userRepository.findAll(pageable);
            List<UserResponseDto> dtos = page.getContent().stream().map(userMapper::toResponse).collect(Collectors.toList());
            return new PageImpl<>(dtos, pageable, page.getTotalElements());
        }
    }

    @Override
    public Page<UserResponseDto> getUsersByRole(Pageable pageable, Role role) {
        Page<User> page = userRepository.findByRole(role, pageable);
        List<UserResponseDto> dtos = page.getContent().stream().map(userMapper::toResponse).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }


    @Override
    public Optional<SellerResponseDto> getSellerProfile(Long sellerId) throws UserNotFoundException {
        if (sellerId == null) throw new UserNotFoundException("Id de vendedor no proporcionado.");
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new UserNotFoundException("Vendedor no encontrado (id=" + sellerId + ")."));

        if (seller.getRole() != Role.SELLER) {
            throw new UserNotFoundException("El usuario indicado no es un vendedor.");
        }


        BigDecimal avg = reviewRepository.getAverageRatingBySellerId(sellerId);
        Integer ratingInt = null;
        if (avg != null) {

            ratingInt = avg.intValue();
        }

        SellerResponseDto dto = new SellerResponseDto(
                seller.getId(),
                seller.getDisplayName(),
                seller.getFirstName(),
                seller.getLastName(),
                seller.getEmail(),
                seller.getRole() == null ? null : seller.getRole(),
                seller.getPhone(),
                seller.getCountry(),
                seller.isActive(),
                seller.getCreatedAt(),
                seller.getLastLogin(),
                ratingInt
        );

        return Optional.of(dto);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public int updateBuyerBalance(Long userId, BigDecimal newBalance) throws UserNotFoundException {
        if (userId == null) throw new UserNotFoundException("Id de usuario no proporcionado.");

        if (newBalance == null || newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El nuevo balance debe ser un valor no negativo.");
        }

        int updatedRows = userRepository.updateBuyerBalance(userId, newBalance);
        if (updatedRows == 0) {
            throw new UserNotFoundException("No se pudo actualizar el balance. Usuario no encontrado (id=" + userId + ").");
        }
        return updatedRows;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public int registerNewLogin(Long userId, Long requestingUserId) throws UserNotFoundException {
        if (userId == null) throw new UserNotFoundException("Id de usuario no proporcionado.");

        if (requestingUserId == null || (!requestingUserId.equals(userId))) {
            throw new UnauthorizedException("No tienes permiso para realizar esta acción.");
        }

        int updatedRows = userRepository.registerNewLogin(userId, java.time.Instant.now());
        if (updatedRows == 0) {
            throw new UserNotFoundException("No se pudo registrar el login. Usuario no encontrado (id=" + userId + ").");
        }
        return updatedRows;
    }
}