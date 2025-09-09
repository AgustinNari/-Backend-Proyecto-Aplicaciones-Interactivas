package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "La orden contiene el mismo producto dentro de más de un ítem, por lo que no se puede completar.")
public class DuplicateOrderItemException extends DuplicateResourceException {
    public DuplicateOrderItemException() {}
    public DuplicateOrderItemException(String message) {}
    public DuplicateOrderItemException(String message, Throwable cause) {}

}
