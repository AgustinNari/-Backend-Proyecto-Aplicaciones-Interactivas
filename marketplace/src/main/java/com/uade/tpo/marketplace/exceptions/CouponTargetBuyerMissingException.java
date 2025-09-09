package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "El cupón no está dirigido a un comprador específico, por lo tanto no es válido.")
public class CouponTargetBuyerMissingException extends BadRequestException {
    public CouponTargetBuyerMissingException(String message) { super(message); }
    public CouponTargetBuyerMissingException() { super(); }
    public CouponTargetBuyerMissingException(String message, Throwable cause) { super(message, cause); }
    
}