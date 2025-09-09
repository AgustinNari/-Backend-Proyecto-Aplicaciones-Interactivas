package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "El código de cupón ya existe.")
public class DuplicateCouponException extends DuplicateResourceException {
    public DuplicateCouponException(String message) { super(message); }
    public DuplicateCouponException() { super(); }
    public DuplicateCouponException(String message, Throwable cause) { super(message, cause); }

}