package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "La categoria que se intenta agregar ya se encuentra registrada en el sistema.")
public class CategoryDuplicateException extends DuplicateResourceException {
    public CategoryDuplicateException(String message) { super(message); }
    public CategoryDuplicateException() { super(); }
    public CategoryDuplicateException(String message, Throwable cause) { super(message, cause); }

}
