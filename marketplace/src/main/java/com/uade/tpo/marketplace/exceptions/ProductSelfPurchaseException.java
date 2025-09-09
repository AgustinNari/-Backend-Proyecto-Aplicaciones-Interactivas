package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "No se permite comprar un producto propio.")
public class ProductSelfPurchaseException extends ConflictException {
    public ProductSelfPurchaseException(String message) { super(message); }
    public ProductSelfPurchaseException() { super(); }
    public ProductSelfPurchaseException(String message, Throwable cause) { super(message, cause); }
}


