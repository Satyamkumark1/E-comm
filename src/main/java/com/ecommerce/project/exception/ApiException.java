package com.ecommerce.project.exception;

public class ApiException extends  RuntimeException{

    private    static final Long serialVersionUIL = 1L;

    public ApiException() {
    }

    public ApiException(String message) {
        super(message);
    }
}
