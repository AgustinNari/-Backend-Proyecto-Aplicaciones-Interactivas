package com.uade.tpo.marketplace.service.interfaces;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.uade.tpo.marketplace.entity.dto.AuthLoginDto;
import com.uade.tpo.marketplace.entity.dto.AuthResponseDto;
import com.uade.tpo.marketplace.entity.dto.create.UserCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.UserResponseDto;
import com.uade.tpo.marketplace.entity.dto.update.UserUpdateDto;
import com.uade.tpo.marketplace.entity.enums.Role;
import com.uade.tpo.marketplace.exceptions.BadRequestException;
import com.uade.tpo.marketplace.exceptions.DuplicateResourceException;
import com.uade.tpo.marketplace.exceptions.ResourceNotFoundException;
import com.uade.tpo.marketplace.exceptions.UnauthorizedException;

public interface IUserService {



    UserResponseDto register(UserCreateDto dto) throws DuplicateResourceException, BadRequestException;


    AuthResponseDto login(AuthLoginDto dto) throws UnauthorizedException, ResourceNotFoundException;

    Optional<UserResponseDto> getUserById(Long id);

    Optional<UserResponseDto> getUserByMail(String email);

    UserResponseDto updateUser(Long id, UserUpdateDto dto, Long requestingUserId)
            throws ResourceNotFoundException, UnauthorizedException, DuplicateResourceException;

    Page<UserResponseDto> getUsers(Pageable pageable, Optional<Role> roleFilter);
}

