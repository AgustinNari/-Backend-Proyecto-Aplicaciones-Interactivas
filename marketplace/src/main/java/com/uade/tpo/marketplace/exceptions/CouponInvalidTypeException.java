package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "El código proporcionado no corresponde a un cupón válido para esta operación.")
public class CouponInvalidTypeException extends BadRequestException {
    public CouponInvalidTypeException(String message) { super(message); }
    public CouponInvalidTypeException() { super(); }
    public CouponInvalidTypeException(String message, Throwable cause) { super(message, cause); }
}