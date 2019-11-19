package com.eunovo.authservice.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {

    public static String SUCCESS_STATUS = "SUCCESS";
    public static String ERROR_STATUS = "ERROR";

    private String status;
    private String message;
    private User data;

    public boolean isSuccessful() {
        return this.status.equals(SUCCESS_STATUS);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class User {
        private Long id;
        private String username;
    }
}