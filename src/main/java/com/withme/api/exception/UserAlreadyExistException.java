package com.withme.api.exception;

import lombok.Getter;

@Getter
public class UserAlreadyExistException extends RuntimeException {

    private String duplicated;

    public UserAlreadyExistException(String message, String duplicated) {
        super(message);
        this.duplicated = duplicated;
    }
}
