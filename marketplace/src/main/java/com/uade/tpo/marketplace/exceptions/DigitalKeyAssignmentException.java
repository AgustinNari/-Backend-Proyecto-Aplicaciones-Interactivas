package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Error al asignar claves digitales al ítem de la orden.")
public class DigitalKeyAssignmentException extends ConflictException {
    public DigitalKeyAssignmentException(String message) { super(message); }
    public DigitalKeyAssignmentException() { super(); }
    public DigitalKeyAssignmentException(String message, Throwable cause) { super(message, cause); }
}