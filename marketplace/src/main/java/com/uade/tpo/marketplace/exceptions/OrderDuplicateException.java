package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;



@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Esta orden de compra ya se encuentra registrada en el sistema.")
public class OrderDuplicateException extends DuplicateResourceException {
    public OrderDuplicateException(String message) { super(message); }
    public OrderDuplicateException() { super(); }
    public OrderDuplicateException(String message, Throwable cause) { super(message, cause); }

}