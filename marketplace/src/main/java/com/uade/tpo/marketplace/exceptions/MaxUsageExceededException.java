package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Se ha excedido el número máximo de usos para este descuento.")
public class MaxUsageExceededException extends ConflictException {
    public MaxUsageExceededException(String message) {
        super(message);
    }
    public MaxUsageExceededException() { super(); }
}