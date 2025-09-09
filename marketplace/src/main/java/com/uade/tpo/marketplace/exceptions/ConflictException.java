package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Conflicto de negocio.")
public class ConflictException extends RuntimeException {
    public ConflictException(String message) { super(message); }
    public ConflictException() { super(); }
    public ConflictException(String message, Throwable cause) { super(message, cause); }

}
