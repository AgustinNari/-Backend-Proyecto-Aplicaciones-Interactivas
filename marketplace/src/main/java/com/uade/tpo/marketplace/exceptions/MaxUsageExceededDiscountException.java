package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Se ha excedido el número máximo de usos para este descuento.")
public class MaxUsageExceededDiscountException extends RuntimeException {
    public MaxUsageExceededDiscountException(String message) {
        super(message);
    }
}