package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "El cupón o descuento que se intenta aplicar a la compra, no se pudo agregar porque la compra ya contiene otro cupón o descuento.")
public class DiscountConflictException extends Exception{

}
