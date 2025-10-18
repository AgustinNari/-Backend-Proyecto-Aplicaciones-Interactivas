package com.uade.tpo.marketplace.service.interfaces;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.uade.tpo.marketplace.entity.dto.create.UserAvatarCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.SellerDetailResponseDto;
import com.uade.tpo.marketplace.entity.dto.response.SellerResponseDto;
import com.uade.tpo.marketplace.entity.dto.response.UserAvatarDeletionResponseDto;
import com.uade.tpo.marketplace.entity.dto.response.UserAvatarResponseDto;
import com.uade.tpo.marketplace.entity.dto.response.UserResponseDto;
import com.uade.tpo.marketplace.entity.dto.update.UserUpdateDto;
import com.uade.tpo.marketplace.entity.enums.Role;
import com.uade.tpo.marketplace.exceptions.BadRequestException;
import com.uade.tpo.marketplace.exceptions.DuplicateResourceException;
import com.uade.tpo.marketplace.exceptions.ResourceNotFoundException;
import com.uade.tpo.marketplace.exceptions.UnauthorizedException;
import com.uade.tpo.marketplace.exceptions.UserNotFoundException;

public interface IUserService {



        Optional<UserResponseDto> getUserById(Long id);

        Optional<UserResponseDto> getUserByMail(String email);

        UserResponseDto updateUser(Long id, UserUpdateDto dto, Long requestingUserId)
                throws ResourceNotFoundException, UnauthorizedException, DuplicateResourceException;

        Page<UserResponseDto> getAllUsers(Pageable pageable, Optional<Role> roleFilter);

        Page<UserResponseDto> getUsersByRole(Pageable pageable, Role role);

        Optional<SellerResponseDto> getSellerProfile(Long sellerId) throws UserNotFoundException;

        int updateBuyerBalance(Long userId, BigDecimal newBalance) throws UserNotFoundException;

        int registerNewLogin(Long userId, Long requestingUserId) throws UserNotFoundException;
        
        UserAvatarResponseDto uploadAvatar (UserAvatarCreateDto dto, Long requestingUserId)
                throws UserNotFoundException, UnauthorizedException, BadRequestException;

        UserAvatarDeletionResponseDto deleteAvatar (Long userId, Long requestingUserId)
                throws UserNotFoundException, UnauthorizedException;

        Optional<SellerDetailResponseDto> getSellerDetail(Long sellerId);


}

