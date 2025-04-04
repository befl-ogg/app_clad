package com.spf.control.feature.common.exception;

import org.springframework.http.HttpStatus;

public class StudioNotFoundException extends BusinessException {

    public StudioNotFoundException(String studioCode) {
        super("Studio-404", String.format("Studio with code %s not found", studioCode), HttpStatus.NOT_FOUND);
    }
}
