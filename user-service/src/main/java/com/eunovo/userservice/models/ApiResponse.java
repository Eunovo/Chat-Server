package com.eunovo.userservice.models;

import lombok.Getter;

import java.util.*;

public class ApiResponse<T> {
    @Getter
    private String status;
    @Getter
    private String message;
    @Getter
    private T data;
    @Getter
    private List<ApiError> errors;

    private ApiResponse(String status, String message, T data) {
        this.data = data;
        this.status = status;
        this.message = message;
        this.errors = new ArrayList();
    }

    private ApiResponse(String status, String message, List<ApiError> errors) {
        this.data = null;
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<T>("SUCCESS", message, data);
    }

    public static ApiResponse error(String message, List<ApiError> errors) {
        return new ApiResponse("ERROR", message, errors);
    }
}