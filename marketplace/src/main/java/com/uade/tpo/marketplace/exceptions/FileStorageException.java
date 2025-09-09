package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Error al almacenar archivos.")
public class FileStorageException extends RuntimeException {
    public FileStorageException(String message) { super(message); }
    public FileStorageException() { super(); }
    public FileStorageException(String message, Throwable cause) { super(message, cause); }

}
