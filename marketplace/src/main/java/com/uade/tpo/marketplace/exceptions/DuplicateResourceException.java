package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "El recurso ya existe dentro del sistema.")
public class DuplicateResourceException extends ConflictException {
    public DuplicateResourceException(String message) { super(message); }
    public DuplicateResourceException() { super(); }

}
