package com.spf.control.feature.common.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException(String username) {
        super("User-404", String.format("User with username %s not found", username), HttpStatus.NOT_FOUND);
    }
    public UserNotFoundException(Long id) {
        super("User-404", String.format("User with id %d not found", id), HttpStatus.NOT_FOUND);
    }
}
