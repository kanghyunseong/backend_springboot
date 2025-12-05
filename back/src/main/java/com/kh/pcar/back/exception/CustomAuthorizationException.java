package com.kh.pcar.back.exception;

public class CustomAuthorizationException extends RuntimeException {

    public CustomAuthorizationException(String message) {
        super(message);
    }
}