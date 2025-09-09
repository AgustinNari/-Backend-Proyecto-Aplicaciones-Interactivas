package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "El cupón no es aplicable a este ítem o no cumple las condiciones.")
public class CouponNotApplicableException extends BadRequestException {
    public CouponNotApplicableException(String message) { super(message); }
    public CouponNotApplicableException() { super(); }
    public CouponNotApplicableException(String message, Throwable cause) { super(message, cause); }
}