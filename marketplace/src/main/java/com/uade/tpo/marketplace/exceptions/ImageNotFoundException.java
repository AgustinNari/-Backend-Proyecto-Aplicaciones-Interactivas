package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "No se pudo encontrar la imagen solicitada.")
public class ImageNotFoundException extends ResourceNotFoundException {
    public ImageNotFoundException(String message) { super(message); }
    public ImageNotFoundException() { super(); }
    public ImageNotFoundException(String message, Throwable cause) { super(message, cause); }
    
}


