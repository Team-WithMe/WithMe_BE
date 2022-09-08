package com.withme.api.exception;

import lombok.Getter;

@Getter
public class UserNotInTeamException extends RuntimeException {

    public UserNotInTeamException(String message) {
        super(message);
    }
}
