package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "La clave digital solicitada no fue encontrada.")
public class DigitalKeyNotFoundException extends ResourceNotFoundException {
    public DigitalKeyNotFoundException(String message) { super(message); }
    public DigitalKeyNotFoundException() { super(); }
    public DigitalKeyNotFoundException(String message, Throwable cause) { super(message, cause); }
}