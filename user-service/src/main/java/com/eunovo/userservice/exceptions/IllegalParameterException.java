package com.eunovo.userservice.exceptions;

import lombok.Getter;

public class IllegalParameterException extends RuntimeException {
    @Getter
    private String resource;
    @Getter
    private String field;
    @Getter
    private String rejectedValue;
    @Getter
    private String message;
    public IllegalParameterException(String resource, String field, String rejectedValue, String message) {
        super();
        this.resource = resource;
        this.field = field;
        this.message = message;
        this.rejectedValue = rejectedValue;
    }
}