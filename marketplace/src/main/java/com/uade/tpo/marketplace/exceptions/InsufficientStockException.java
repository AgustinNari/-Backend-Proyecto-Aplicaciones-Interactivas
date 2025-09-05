package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Stock insuficiente para la compra.")
public class InsufficientStockException extends ConflictException {
    public InsufficientStockException(String message) {
        super(message);
    }
    public InsufficientStockException() { super(); }

}
