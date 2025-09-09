package com.uade.tpo.marketplace.controllers;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.controllers.auth.CurrentUserProvider;
import com.uade.tpo.marketplace.entity.dto.response.SellerResponseDto;
import com.uade.tpo.marketplace.entity.dto.response.UserResponseDto;
import com.uade.tpo.marketplace.entity.dto.update.UserUpdateDto;
import com.uade.tpo.marketplace.entity.enums.Role;
import com.uade.tpo.marketplace.service.interfaces.IUserService;

@RestController
@RequestMapping("/users")

public class UsersController {
    @Autowired
    private IUserService userService;
    @Autowired
    private CurrentUserProvider authenticator;

    @GetMapping("{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id){
        Optional<UserResponseDto> result =  userService.getUserById(id);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("{userId}")
    public ResponseEntity<UserResponseDto> getUserByMail(@PathVariable String mail){
        Optional<UserResponseDto> result =  userService.getUserByMail(mail);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("{userId}")
    public Page<UserResponseDto> getUsers(@PathVariable Pageable pageable, Optional<Role> roleFilter){
        Page<UserResponseDto> result = userService.getUsers(pageable, roleFilter);
        return result;
    }

    @GetMapping("{userId}")
    public Page<UserResponseDto> getUsersByRole(@PathVariable Pageable pageable, Role role) {
        Page<UserResponseDto> result = userService.getUsersByRole(pageable, role);
        return result;
    }

    @GetMapping("{sellerId}")
    public Optional<SellerResponseDto> getSellerProfile(@PathVariable Long sellerId) {
        Optional<SellerResponseDto> result = userService.getSellerProfile(sellerId);
        return result;
    }

    @GetMapping("{userId}")
    public int registerNewLogin(@PathVariable Long userId, Authentication authentication) {
        Long requestingUserId = authenticator.getCurrentUserId(authentication);
        return userService.registerNewLogin(userId, requestingUserId);
    }

    @PostMapping("/{userId}/update")
    public UserResponseDto updateUser(@PathVariable Long id, @RequestBody UserUpdateDto userUpdateDto, Authentication authentication){
        Long requestingUserId = authenticator.getCurrentUserId(authentication);
        UserResponseDto result =  userService.updateUser(id, userUpdateDto, requestingUserId);
        return result;
    }

    @PostMapping("{userId}/updateBalance")
    public int updateBuyerBalance(@PathVariable Long userId, BigDecimal newBalance) {
        return userService.updateBuyerBalance(userId, newBalance);
    }
}//Enrique Busso
