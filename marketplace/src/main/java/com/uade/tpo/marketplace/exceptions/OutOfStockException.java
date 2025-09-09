package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//Valentin: Se vende la clave de un videojuego al que un usuario quiere acceder

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "El producto no tiene stock.")
public class OutOfStockException extends InsufficientStockException {
    public OutOfStockException(String message) { super(message); }
    public OutOfStockException() { super(); }
    public OutOfStockException(String message, Throwable cause) { super(message, cause); }

}