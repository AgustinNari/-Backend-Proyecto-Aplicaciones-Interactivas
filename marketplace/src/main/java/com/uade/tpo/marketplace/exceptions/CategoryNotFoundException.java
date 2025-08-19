package com.uade.tpo.marketplace.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "No se pudo encontrar la categoria indicada.")
public class CategoryNotFoundException {
    
}
