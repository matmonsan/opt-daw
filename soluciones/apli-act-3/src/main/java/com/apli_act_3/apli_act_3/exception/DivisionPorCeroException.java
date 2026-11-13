package com.apli_act_3.apli_act_3.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DivisionPorCeroException extends ResponseStatusException {
    public DivisionPorCeroException(String mensaje) {
        super(HttpStatus.BAD_REQUEST, mensaje);
    }
}
