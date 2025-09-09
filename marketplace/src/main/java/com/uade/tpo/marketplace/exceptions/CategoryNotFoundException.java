package com.uade.tpo.marketplace.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "No se pudo encontrar la categoria indicada.")
public class CategoryNotFoundException  extends ResourceNotFoundException {
    public CategoryNotFoundException(String message) { super(message); }
    public CategoryNotFoundException() { super(); }
    public CategoryNotFoundException(String message, Throwable cause) { super(message, cause); }
    
}
