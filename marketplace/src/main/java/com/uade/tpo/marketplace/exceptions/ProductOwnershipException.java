package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Operación no permitida sobre el producto (propiedad/permiso).")
public class ProductOwnershipException extends ForbiddenException {
    public ProductOwnershipException(String message) { super(message); }
    public ProductOwnershipException() { super(); }
    public ProductOwnershipException(String message, Throwable cause) { super(message, cause); }
}