package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//Valentin: Un usuario intenta subir una clave de videojuego repetida

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "La clave que se intenta agregar ya se encuentra registrada en el sistema.")
public class DigitalKeyDuplicateException extends Exception {

}