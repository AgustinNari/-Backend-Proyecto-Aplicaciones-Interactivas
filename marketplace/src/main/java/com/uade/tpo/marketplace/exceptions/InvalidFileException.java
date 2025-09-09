package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Archivo inválido o formato no soportado.")
public class InvalidFileException extends BadRequestException {
    public InvalidFileException(String message) { super(message); }
    public InvalidFileException() { super(); }
    public InvalidFileException(String message, Throwable cause) { super(message, cause); }
}