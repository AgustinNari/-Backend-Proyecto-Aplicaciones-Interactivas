package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//Valentin: Una orden de compra posee el mismo ID que una orden de compra ya existente

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Esta orden de compra ya se encuentra registrada en el sistema.")
public class OrderDuplicateException extends DuplicateResourceException {
    public OrderDuplicateException(String message) { super(message); }
    public OrderDuplicateException() { super(); }

}