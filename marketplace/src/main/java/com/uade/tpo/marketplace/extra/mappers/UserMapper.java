package com.uade.tpo.marketplace.extra.mappers;

import org.springframework.stereotype.Component;

import com.uade.tpo.marketplace.entity.basic.User;
import com.uade.tpo.marketplace.entity.dto.create.UserCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.UserResponseDto;
import com.uade.tpo.marketplace.entity.dto.update.UserUpdateDto;
import com.uade.tpo.marketplace.entity.enums.Role;

@Component
public class UserMapper {

    public User toEntity(UserCreateDto dto){
        if (dto == null) return null;
        User u = new User();
        u.setUsername(dto.username());
        u.setEmail(dto.email());
        if (dto.role() != null) {
            try { u.setRole(Role.valueOf(dto.role())); } catch (Exception ignored) {}
        }
        u.setDisplayName(dto.displayName());
        u.setPhone(dto.phone());
        u.setCountry(dto.country());
        return u;
    }

    public void updateFromDto(UserUpdateDto dto, User entity){
        if (dto == null || entity == null) return;
        if (dto.email() != null) entity.setEmail(dto.email());
        if (dto.displayName() != null) entity.setDisplayName(dto.displayName());
        if (dto.phone() != null) entity.setPhone(dto.phone());
        if (dto.country() != null) entity.setCountry(dto.country());
        if (dto.active() != null) entity.setActive(dto.active());

    }

    public UserResponseDto toResponse(User u){
        if (u == null) return null;
        int productCount = u.getProducts() == null ? 0 : u.getProducts().size();
        return new UserResponseDto(
            u.getId(), u.getUsername(), u.getEmail(),
            u.getRole() == null ? null : u.getRole().name(),
            u.getDisplayName(), u.getPhone(), u.getCountry(),
            u.isActive(), u.getCreatedAt(), u.getLastLogin(), u.getSellerBalance(),
            productCount
        );
    }
}