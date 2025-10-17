package com.uade.tpo.marketplace.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.uade.tpo.marketplace.entity.basic.User;
import com.uade.tpo.marketplace.entity.dto.create.UserAvatarCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.SellerResponseDto;
import com.uade.tpo.marketplace.entity.dto.response.UserAvatarDeletionResponseDto;
import com.uade.tpo.marketplace.entity.dto.response.UserAvatarResponseDto;
import com.uade.tpo.marketplace.entity.dto.response.UserResponseDto;
import com.uade.tpo.marketplace.entity.dto.update.UserUpdateDto;
import com.uade.tpo.marketplace.entity.enums.Role;
import com.uade.tpo.marketplace.exceptions.BadRequestException;
import com.uade.tpo.marketplace.exceptions.DuplicateResourceException;
import com.uade.tpo.marketplace.exceptions.ImageProcessingException;
import com.uade.tpo.marketplace.exceptions.ResourceNotFoundException;
import com.uade.tpo.marketplace.exceptions.UnauthorizedException;
import com.uade.tpo.marketplace.exceptions.UserDuplicateException;
import com.uade.tpo.marketplace.exceptions.UserNotFoundException;
import com.uade.tpo.marketplace.extra.mappers.UserAvatarMapper;
import com.uade.tpo.marketplace.extra.mappers.UserMapper;
import com.uade.tpo.marketplace.repository.interfaces.IReviewRepository;
import com.uade.tpo.marketplace.repository.interfaces.IUserRepository;
import com.uade.tpo.marketplace.service.interfaces.IUserService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class UserService implements IUserService {

    private static final long MAX_AVATAR_BYTES = 5 * 1024 * 1024; // 5 MB
    private static final Set<String> ALLOWED_MIMES = Set.of("image/jpeg", "image/png", "image/gif", "image/svg+xml");

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IReviewRepository reviewRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserAvatarMapper avatarMapper;

    @PersistenceContext
    private EntityManager entityManager;




    
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

        if (id == null) throw new BadRequestException("Id de usuario no proporcionado.");
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Usuario no encontrado (id=" + id + ")."));


        if (requestingUserId == null || (!requestingUserId.equals(id))) {
            throw new UnauthorizedException("No tienes permiso para realizar esta acción.");
        }


        if (dto.displayName() != null && !dto.displayName().isBlank()) {
            String newDisplay = dto.displayName().trim();
            String currentDisplay = existing.getDisplayName();
            if (currentDisplay == null || !currentDisplay.equalsIgnoreCase(newDisplay)) {
                boolean exists = userRepository.existsByDisplayNameIgnoreCase(newDisplay);
                if (exists) {
                    throw new UserDuplicateException("Ya existe otro usuario con ese nombre de usuario (displayName).");
                }
            }
        }

        userMapper.updateFromDto(dto, existing);

        User saved = userRepository.save(existing);
        return userMapper.toResponse(saved);
    }

    @Override
    public Page<UserResponseDto> getAllUsers(Pageable pageable, Optional<Role> roleFilter) {
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
        if (sellerId == null) throw new BadRequestException("Id de vendedor no proporcionado.");
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new UserNotFoundException("Vendedor no encontrado (id=" + sellerId + ")."));

        if (seller.getRole() != Role.SELLER) {
            throw new BadRequestException("El usuario indicado no es un vendedor.");
        }


        BigDecimal avg = reviewRepository.getAverageRatingBySellerId(sellerId);

        UserAvatarResponseDto avatar = avatarMapper.toResponse(seller);

        SellerResponseDto dto = new SellerResponseDto(
            seller.getId(),
            seller.getDisplayName(),
            seller.getSellerDescription(),
            seller.getFirstName(),
            seller.getLastName(),
            seller.getEmail(),
            seller.getPhone(),
            seller.getCountry(),
            seller.isActive(),
            avg,
            avatar == null ? null : avatar.contentType(),
            avatar == null ? null : avatar.dataUrl()
        );
                
        return Optional.of(dto);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public int updateBuyerBalance(Long userId, BigDecimal newBalance) throws UserNotFoundException {
        if (userId == null) throw new BadRequestException("Id de usuario no proporcionado.");

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
        if (userId == null) throw new BadRequestException("Id de usuario no proporcionado.");

        if (requestingUserId == null || (!requestingUserId.equals(userId))) {
            throw new UnauthorizedException("No tienes permiso para realizar esta acción.");
        }

        int updatedRows = userRepository.registerNewLogin(userId, java.time.Instant.now());
        if (updatedRows == 0) {
            throw new UserNotFoundException("No se pudo registrar el login. Usuario no encontrado (id=" + userId + ").");
        }
        return updatedRows;
    }


    @Override
    @Transactional(rollbackFor = Throwable.class)
    public UserAvatarResponseDto uploadAvatar(UserAvatarCreateDto dto, Long requestingUserId)
            throws UserNotFoundException, UnauthorizedException, BadRequestException {

        if (dto == null || dto.userId() == null) {
            throw new BadRequestException("UserId no proporcionado.");
        }
        if (dto.file() == null || dto.file().isEmpty()) {
            throw new BadRequestException("Archivo no proporcionado o vacío.");
        }

        Long targetUserId = dto.userId();


        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado (id=" + targetUserId + ")."));


        if (requestingUserId == null) {
            throw new UnauthorizedException("No autenticado.");
        }
        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new UserNotFoundException("Usuario solicitante no encontrado (id=" + requestingUserId + ")."));

        boolean isOwner = Objects.equals(requestingUserId, targetUserId);
        boolean isAdmin = requestingUser.getRole() == Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new UnauthorizedException("No tienes permiso para subir el avatar de este usuario.");
        }

        MultipartFile file = dto.file();


        if (file.getSize() > MAX_AVATAR_BYTES) {
            throw new BadRequestException("Archivo demasiado grande. Límite: " + (MAX_AVATAR_BYTES/1024/1024) + " MB.");
        }


        User tmp = avatarMapper.toEntityAvatar(targetUserId, file);
        if (tmp == null || tmp.getAvatar() == null) {
            throw new ImageProcessingException("No se pudo procesar el archivo de avatar.");
        }

        String mime = tmp.getAvatarContentType();
        if (mime == null) mime = "application/octet-stream";


        if (!mime.startsWith("image/") && !ALLOWED_MIMES.contains(mime)) {
            throw new BadRequestException("Tipo de archivo no permitido: " + mime);
        }


        int updated = userRepository.uploadAvatarById(targetUserId, tmp.getAvatar(), mime);
        if (updated == 0) {
            throw new UserNotFoundException("No se pudo guardar el avatar. Usuario no encontrado (id=" + targetUserId + ").");
        }

        

        User saved = userRepository.findById(targetUserId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado tras guardar el avatar (id=" + targetUserId + ")."));

        entityManager.refresh(saved);
        
        
        return avatarMapper.toResponse(saved);
    }


    @Override
    @Transactional(rollbackFor = Throwable.class)
    public UserAvatarDeletionResponseDto deleteAvatar(Long userId, Long requestingUserId)
            throws UserNotFoundException, UnauthorizedException {

        if (userId == null) throw new BadRequestException("UserId no proporcionado.");

        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado (id=" + userId + ")."));

        if (requestingUserId == null) throw new UnauthorizedException("No autenticado.");

        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new UserNotFoundException("Usuario solicitante no encontrado (id=" + requestingUserId + ")."));

        boolean isOwner = Objects.equals(requestingUserId, userId);
        boolean isAdmin = requestingUser.getRole() == Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new UnauthorizedException("No tienes permiso para eliminar el avatar de este usuario.");
        }

        int updated = userRepository.deleteAvatarById(userId);
        if (updated == 0) {
            throw new UserNotFoundException("No se pudo eliminar el avatar. Usuario no encontrado (id=" + userId + ").");
        }

        return new UserAvatarDeletionResponseDto(true, userId, "Avatar eliminado correctamente.");
    }
}