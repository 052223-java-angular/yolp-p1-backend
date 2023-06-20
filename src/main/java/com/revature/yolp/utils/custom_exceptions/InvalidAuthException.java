package com.revature.yolp.utils.custom_exceptions;

public class InvalidAuthException extends RuntimeException {
    public InvalidAuthException(String message) {
        super(message);
    }
}
