package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "El producto no está activo.")
public class ProductNotActiveException extends BadRequestException {
    public ProductNotActiveException(String message) {
        super(message);
    }
    public ProductNotActiveException() { super(); }
    public ProductNotActiveException(String message, Throwable cause) { super(message, cause); }
}