package com.systex.test.demo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class AgriProductsTransTypeException extends RuntimeException {
    public AgriProductsTransTypeException(String message){
        super(message);
    }
}

