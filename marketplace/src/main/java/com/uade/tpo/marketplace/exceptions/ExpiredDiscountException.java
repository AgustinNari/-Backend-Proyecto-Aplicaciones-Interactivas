package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "El descuento ha expirado.")
public class ExpiredDiscountException extends BadRequestException {
    public ExpiredDiscountException(String message) {
        super(message);
    }
    public ExpiredDiscountException() { super(); }
    public ExpiredDiscountException(String message, Throwable cause) { super(message, cause); }
}