package com.matan.api.exceptions;

/*
This error is being thrown when a client sent invalid data in the request (code 400)
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}