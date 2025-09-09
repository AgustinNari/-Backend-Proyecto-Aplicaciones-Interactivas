package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "El descuento es inválido o no aplicable.")
public class InvalidDiscountException extends BadRequestException {
    public InvalidDiscountException(String message) {
        super(message);
    }
    public InvalidDiscountException() { super(); }
    public InvalidDiscountException(String message, Throwable cause) { super(message, cause); }
}