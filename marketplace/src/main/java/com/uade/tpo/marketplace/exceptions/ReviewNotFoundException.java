package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "La reseña solicitada no fue encontrada.")
public class ReviewNotFoundException extends ResourceNotFoundException {
    public ReviewNotFoundException(String message) {
        super(message);
    }
    public ReviewNotFoundException() { super(); }
}