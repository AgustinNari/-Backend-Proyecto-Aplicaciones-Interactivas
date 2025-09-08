package com.uade.tpo.marketplace.extra.mappers;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.uade.tpo.marketplace.entity.basic.User;
import com.uade.tpo.marketplace.entity.dto.create.UserCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.BuyerResponseDto;
import com.uade.tpo.marketplace.entity.dto.response.SellerResponseDto;
import com.uade.tpo.marketplace.entity.dto.response.UserResponseDto;
import com.uade.tpo.marketplace.entity.dto.update.UserUpdateDto;

@Component
public class UserMapper {

    public User toEntity(UserCreateDto dto){
        if (dto == null) return null;
        User u = new User();
        u.setDisplayName(dto.displayName());
        u.setFirstName(dto.firstName()); 
        u.setLastName(dto.lastName());
        u.setEmail(dto.email());
        if (dto.role() != null) u.setRole(dto.role());
        u.setPhone(dto.phone());
        u.setCountry(dto.country());
        return u;
    }

    public void updateFromDto(UserUpdateDto dto, User entity){
        if (dto == null || entity == null) return;
        if (dto.displayName() != null) entity.setDisplayName(dto.displayName());
        if (dto.firstName() != null) entity.setFirstName(dto.firstName());
        if (dto.lastName() != null) entity.setLastName(dto.lastName());
        if (dto.phone() != null) entity.setPhone(dto.phone());
        if (dto.country() != null) entity.setCountry(dto.country());
    }

    public UserResponseDto toResponse(User u){
        if (u == null) return null;
        return new UserResponseDto(
            u.getId(),
            u.getDisplayName(),
            u.getFirstName(),
            u.getLastName(),
            u.getEmail(),
            u.getRole(),
            u.getPhone(),
            u.getCountry(),
            u.isActive(),
            u.getCreatedAt(),
            u.getLastLogin()
        );
    }
    public SellerResponseDto toSellerResponse(User u){
        if (u == null) return null;
        return new SellerResponseDto(
            u.getId(),
            u.getDisplayName(),
            u.getFirstName(),
            u.getLastName(),
            u.getEmail(),
            u.getRole(),
            u.getPhone(),
            u.getCountry(),
            u.isActive(),
            u.getCreatedAt(),
            u.getLastLogin(),
            u.getSellerRating()
        );
    }


    public BuyerResponseDto toBuyerResponse(User u){
        if (u == null) return null;
        return new BuyerResponseDto(
            u.getId(),
            u.getDisplayName(),
            u.getFirstName(),
            u.getLastName(),
            u.getEmail(),
            u.getRole(),
            u.getPhone(),
            u.getCountry(),
            u.isActive(),
            u.getCreatedAt(),
            u.getLastLogin(),
            u.getBuyerBalance()
        );
    }


    public User fromId(Long id) {
        if (id == null) return null;
        User u = new User();
        u.setId(id);
        return u;
    }

    public List<UserResponseDto> toResponseList(Collection<User> users) {
        if (users == null || users.isEmpty()) return Collections.emptyList();
        return users.stream()
                .filter(Objects::nonNull)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}