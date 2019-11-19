package com.eunovo.authservice.models;

import java.io.Serializable;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse<T> implements Serializable {
    private String status;
    private String message;
    private T data;

    public static <T> JwtResponse<T> success(String message, T data) {
        return new JwtResponse<>("SUCCESS", message, data);
    }

    public static <T> JwtResponse<T> error(String message) {
        return new JwtResponse<>("ERROR", message, null);
    }
}