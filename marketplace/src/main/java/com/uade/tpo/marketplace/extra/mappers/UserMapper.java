package com.uade.tpo.marketplace.extra.mappers;

import org.springframework.stereotype.Component;

import com.uade.tpo.marketplace.entity.basic.User;
import com.uade.tpo.marketplace.entity.dto.create.UserCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.BuyerResponseDto;
import com.uade.tpo.marketplace.entity.dto.response.SellerResponseDto;
import com.uade.tpo.marketplace.entity.dto.response.UserResponseDto;
import com.uade.tpo.marketplace.entity.dto.update.UserUpdateDto;
import com.uade.tpo.marketplace.entity.enums.Role;

@Component
public class UserMapper {

    public User toEntity(UserCreateDto dto){
        if (dto == null) return null;
        User u = new User();
        u.setDisplayName(dto.displayName());
        u.setFirstName(dto.firstName());
        u.setLastName(dto.lastName());
        u.setEmail(dto.email());
        if (dto.role() != null) {
            try { u.setRole(Role.valueOf(dto.role())); } catch (Exception ignored) {}
        }
        u.setPhone(dto.phone());
        u.setCountry(dto.country());
        return u;
    }

    public void updateFromDto(UserUpdateDto dto, User entity){
        if (dto == null || entity == null) return;
        if (dto.email() != null) entity.setEmail(dto.email());
        if (dto.phone() != null) entity.setPhone(dto.phone());
        if (dto.country() != null) entity.setCountry(dto.country());
        if (dto.active() != null) entity.setActive(dto.active());

    }

    public UserResponseDto toResponse(User u){
        if (u == null) return null;
        return new UserResponseDto(
            u.getId(), u.getDisplayName(), u.getFirstName(), u.getLastName(), u.getEmail(),
            u.getRole() == null ? null : u.getRole().name(),
            u.getPhone(), u.getCountry(),
            u.isActive(), u.getCreatedAt(), u.getLastLogin()
        );
    }

    public SellerResponseDto toSellerResponse(User u){
        if (u == null) return null;
        return new SellerResponseDto(
            u.getId(), u.getDisplayName(), u.getFirstName(), u.getLastName(), u.getEmail(),
            u.getRole() == null ? null : u.getRole().name(),
            u.getPhone(), u.getCountry(),
            u.isActive(), u.getCreatedAt(), u.getLastLogin(),
            u.getSellerRating()
        );
    }

    public BuyerResponseDto toBuyerResponse(User u){
        if (u == null) return null;
        return new BuyerResponseDto(
            u.getId(), u.getDisplayName(), u.getFirstName(), u.getLastName(), u.getEmail(),
            u.getRole() == null ? null : u.getRole().name(),
            u.getPhone(), u.getCountry(),
            u.isActive(), u.getCreatedAt(), u.getLastLogin()
        );
    }
}