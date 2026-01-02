package com.hcmute.fme.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    private final HttpStatus status;
    private final String code;

    public ApiException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
        this.code = "BAD_REQUEST";
    }

    public ApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.code = status.name();
    }

    public ApiException(String message, HttpStatus status, String code) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}
