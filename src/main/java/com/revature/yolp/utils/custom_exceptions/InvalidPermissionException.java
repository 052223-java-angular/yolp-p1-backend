package com.revature.yolp.utils.custom_exceptions;

public class InvalidPermissionException extends RuntimeException {
    public InvalidPermissionException(String message) {
        super(message);
    }
}
