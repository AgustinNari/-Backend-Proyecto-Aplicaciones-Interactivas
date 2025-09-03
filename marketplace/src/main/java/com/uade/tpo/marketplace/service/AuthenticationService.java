
package com.uade.tpo.marketplace.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.marketplace.controllers.auth.AuthenticationRequest;
import com.uade.tpo.marketplace.controllers.auth.AuthenticationResponse;
import com.uade.tpo.marketplace.controllers.auth.RegisterRequest;
import com.uade.tpo.marketplace.controllers.config.JwtService;
import com.uade.tpo.marketplace.entity.basic.User;
import com.uade.tpo.marketplace.exceptions.ResourceNotFoundException;
import com.uade.tpo.marketplace.repository.interfaces.IUserRepository;
import com.uade.tpo.marketplace.service.interfaces.IAuthenticationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService{

        private final IUserRepository repository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;

        @Transactional (rollbackFor = Throwable.class)
        public AuthenticationResponse register(RegisterRequest request) {
                var user = new User(
                                request.getDisplayName(),
                                request.getFirstName(),
                                request.getLastName(),
                                request.getEmail(),
                                passwordEncoder.encode(request.getPassword()),
                                request.getRole(),
                                request.getCountry()
                        );

                repository.save(user);
                var jwtToken = jwtService.generateToken( user);
                return AuthenticationResponse.builder()
                                .accessToken(jwtToken)
                                .build();
        }

        public AuthenticationResponse authenticate(AuthenticationRequest request) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getEmail(),
                                                request.getPassword()));
                var user = repository.findByEmail(request.getEmail())
                                .orElseThrow();
                var jwtToken = jwtService.generateToken(user);
                return AuthenticationResponse.builder()
                                .accessToken(jwtToken)
                                .build();
        }

        @Override
        public void changePassword(Long userId, String currentPassword, String newPassword) throws ResourceNotFoundException {
                // TODO Auto-generated method stub
        }
        
}
