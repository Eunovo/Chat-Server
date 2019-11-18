package com.eunovo.authservice.models;

import java.io.Serializable;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse implements Serializable {
    private String status;
    private String message;
    private String token;

    public static JwtResponse success(String message, String token) {
        return new JwtResponse("SUCCESS", message, token);
    }

    public static JwtResponse error(String message) {
        return new JwtResponse("ERROR", message, "");
    }
}