package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Credenciales inválidas o no autorizadas para realizar esta acción.")
public class UnauthorizedException extends Exception {

}
