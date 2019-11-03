package com.eunovo.userservice.models;

import lombok.Getter;

import java.util.List;

public class ApiResponse<T> {
    @Getter
    private String status;
    @Getter
    private String message;
    @Getter
    private T data;

    private ApiResponse(String status, String message, T data) {
        this.data = data;
        this.status = status;
        this.message = message;
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<T>("SUCCESS", message, data);
    }
}