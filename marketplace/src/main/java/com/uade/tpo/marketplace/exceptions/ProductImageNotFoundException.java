package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Imagen de producto no encontrada.")
public class ProductImageNotFoundException extends RuntimeException {
    public ProductImageNotFoundException(String message) { super(message); }
    public ProductImageNotFoundException() { super(); }
    public ProductImageNotFoundException(String message, Throwable cause) { super(message, cause); }
}
