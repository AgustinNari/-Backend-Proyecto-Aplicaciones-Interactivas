package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "El recurso solicitado no fue encontrado en el sistema.")
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) { super(message); }
    public ResourceNotFoundException() { super(); }
    public ResourceNotFoundException(String message, Throwable cause) { super(message, cause); }
}
