package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;



@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "El formato de esta clave digital es inválida.")
public class InvalidDigitalKeyFormat extends BadRequestException {
    public InvalidDigitalKeyFormat(String message) { super(message); }
    public InvalidDigitalKeyFormat() { super(); }
    public InvalidDigitalKeyFormat(String message, Throwable cause) { super(message, cause); }

}