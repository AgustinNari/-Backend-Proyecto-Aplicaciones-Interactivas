package com.uade.tpo.marketplace.controllers.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.uade.tpo.marketplace.exceptions.UnauthorizedException;
import com.uade.tpo.marketplace.exceptions.UserNotFoundException;
import com.uade.tpo.marketplace.repository.interfaces.IUserRepository;

@Component
public class CurrentUserProvider {

    @Autowired
    private IUserRepository userRepository;

    

    public Long getCurrentUserId(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new UnauthorizedException("No autenticado.");
        }

        final String resolvedEmail;
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails ud) {
            resolvedEmail = ud.getUsername();
        } else {
            resolvedEmail = authentication.getName();
        }

        return userRepository.findByEmailIgnoreCase(resolvedEmail)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con email=" + resolvedEmail))
                .getId();
    }
}