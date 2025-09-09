package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Error al procesar la imagen.")
public class ImageProcessingException extends RuntimeException {
    public ImageProcessingException(String message) { super(message); }
    public ImageProcessingException() { super(); }
    public ImageProcessingException(String message, Throwable cause) { super(message, cause); }
}