package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//Valentin: Se vende la clave de un videojuego al que un usuario quiere acceder

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "La clave del videojuego que intentaba conseguir ya ha sido vendida.")
public class OutOfStockException extends Exception {

}