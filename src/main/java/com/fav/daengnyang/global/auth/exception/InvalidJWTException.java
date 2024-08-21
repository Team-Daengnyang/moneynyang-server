package com.fav.daengnyang.global.auth.exception;

public class InvalidJWTException extends RuntimeException {

    public InvalidJWTException() {
    }

    public InvalidJWTException(String message) {
        super(message);
    }

    public InvalidJWTException(String message, Throwable cause) {
        super(message, cause);
    }
}