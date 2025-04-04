package com.spf.control.feature.common.exception;

import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.Map;

public abstract class BusinessException extends RuntimeException{

    private final HttpStatus httpStatus;
    private final String errorCode;

    protected BusinessException(String errorCode, String message, HttpStatus httpStatus){
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus(){
        return this.httpStatus;
    }

    public Map<String, Object> getData(){
        return Map.of(
                "errorCode", this.errorCode,
                "message", this.getMessage(),
                "httpStatus", this.getHttpStatus(),
                "timestamp", new Date().getTime()
        );
    }

}
