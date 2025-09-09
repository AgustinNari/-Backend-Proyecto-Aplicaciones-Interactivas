package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "El cupón ya fue utilizado.")
public class CouponAlreadyUsedException extends ConflictException {
    public CouponAlreadyUsedException(String message) { super(message); }
    public CouponAlreadyUsedException() { super(); }
    public CouponAlreadyUsedException(String message, Throwable cause) { super(message, cause); }
}