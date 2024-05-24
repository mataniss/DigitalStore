package com.matan.api.exceptions;

/*This error is being thrown when a client has tried to access a resource
he is not allowed to (code 401)
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}