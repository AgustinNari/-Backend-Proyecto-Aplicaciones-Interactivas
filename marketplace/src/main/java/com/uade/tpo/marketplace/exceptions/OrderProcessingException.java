package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Error al procesar la orden.")
public class OrderProcessingException extends RuntimeException {
    public OrderProcessingException(String message) { super(message); }
    public OrderProcessingException() { super(); }
    public OrderProcessingException(String message, Throwable cause) { super(message, cause); }
}