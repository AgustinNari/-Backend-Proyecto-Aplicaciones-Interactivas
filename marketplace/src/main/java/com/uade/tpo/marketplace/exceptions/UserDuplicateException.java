package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//Valentin: Un usuario intenta registrarse con el mismo nombre de un usuario ya existente

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "El usuario que intenta crear ya se encuentra registrado en el sistema.")
public class UserDuplicateException extends Exception {

}