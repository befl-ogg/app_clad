package com.spf.control.feature.common.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends BusinessException{

    public UserAlreadyExistsException(String username) {
        super("User-400", String.format("User with username %s already exists", username), HttpStatus.BAD_REQUEST);
    }
}
