package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//Valentin: Una clave posee un formato diferente al esperado

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "El formato de esta clave digital es inválida.")
public class InvalidDigitalKeyFormat extends Exception {

}