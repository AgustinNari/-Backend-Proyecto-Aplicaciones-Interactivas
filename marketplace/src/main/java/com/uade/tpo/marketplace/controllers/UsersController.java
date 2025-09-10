package com.uade.tpo.marketplace.controllers;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.controllers.auth.CurrentUserProvider;
import com.uade.tpo.marketplace.entity.dto.response.SellerResponseDto;
import com.uade.tpo.marketplace.entity.dto.response.UserResponseDto;
import com.uade.tpo.marketplace.entity.dto.update.UserUpdateDto;
import com.uade.tpo.marketplace.entity.enums.Role;
import com.uade.tpo.marketplace.service.interfaces.IUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")

public class UsersController {
    @Autowired
    private IUserService userService;
    @Autowired
    private CurrentUserProvider authenticator;


    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(Pageable pageable,
                                                        @RequestParam(required = false) Optional<Role> role) {
        Page<UserResponseDto> page = userService.getAllUsers(pageable, role);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable("id") Long id) {
        Optional<UserResponseDto> result = userService.getUserById(id);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/by-email")
    public ResponseEntity<UserResponseDto> getUserByMail(@RequestParam("email") String email) {
        Optional<UserResponseDto> result = userService.getUserByMail(email);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<Page<UserResponseDto>> getUsersByRole(@PathVariable("role") Role role, Pageable pageable) {
        Page<UserResponseDto> page = userService.getUsersByRole(pageable, role);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/seller/{sellerId}/profile")
    public ResponseEntity<SellerResponseDto> getSellerProfile(@PathVariable("sellerId") Long sellerId) {
        Optional<SellerResponseDto> opt = userService.getSellerProfile(sellerId);
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/{id}/register-login")
    public ResponseEntity<Integer> registerNewLogin(@PathVariable("id") Long userId, Authentication authentication) {
        Long requestingUserId = authenticator.getCurrentUserId(authentication);
        int result = userService.registerNewLogin(userId, requestingUserId);
        return ResponseEntity.ok(result);
    }
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable("id") Long id,
                                                      @Valid @RequestBody UserUpdateDto userUpdateDto,
                                                      Authentication authentication) {
        Long requestingUserId = authenticator.getCurrentUserId(authentication);
        UserResponseDto updated = userService.updateUser(id, userUpdateDto, requestingUserId);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/me/balance")
    public ResponseEntity<Integer> updateBuyerBalance(@RequestParam("newBalance") BigDecimal newBalance,
                                                      Authentication authentication) {
        Long userId = authenticator.getCurrentUserId(authentication);
        int updated = userService.updateBuyerBalance(userId, newBalance);
        return ResponseEntity.ok(updated);
    }
}
