package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Ya se realizó una reseña para este ítem de compra, por lo que no puede crear una nueva.")
public class DuplicateReviewException extends DuplicateResourceException{
    public DuplicateReviewException(String message) { super(message); }
    public DuplicateReviewException() { super(); }
    public DuplicateReviewException(String message, Throwable cause) { super(message, cause); }
}
