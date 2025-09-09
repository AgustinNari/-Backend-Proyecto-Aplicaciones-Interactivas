package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "La orden (o ítem) solicitada no fue encontrada.")
public class OrderNotFoundException extends ResourceNotFoundException {
    public OrderNotFoundException(String message) {
        super(message);
    }
    public OrderNotFoundException() { super(); }
    public OrderNotFoundException(String message, Throwable cause) { super(message, cause); }
}