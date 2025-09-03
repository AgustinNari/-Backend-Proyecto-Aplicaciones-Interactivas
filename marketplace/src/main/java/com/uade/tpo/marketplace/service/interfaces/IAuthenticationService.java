package com.uade.tpo.marketplace.service.interfaces;

import com.uade.tpo.marketplace.controllers.auth.AuthenticationRequest;
import com.uade.tpo.marketplace.controllers.auth.AuthenticationResponse;
import com.uade.tpo.marketplace.controllers.auth.RegisterRequest;
import com.uade.tpo.marketplace.exceptions.BadRequestException;
import com.uade.tpo.marketplace.exceptions.ResourceNotFoundException;
import com.uade.tpo.marketplace.exceptions.UnauthorizedException;

public interface IAuthenticationService {

    public AuthenticationResponse register(RegisterRequest request);

    public AuthenticationResponse authenticate(AuthenticationRequest request);

    void changePassword(Long userId, String currentPassword, String newPassword) throws ResourceNotFoundException, UnauthorizedException, BadRequestException;

}
