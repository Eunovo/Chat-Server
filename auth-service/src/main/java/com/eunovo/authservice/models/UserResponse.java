package com.eunovo.authservice.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {
    private String status;
    private String message;
    private User data;

    @Data
    @NoArgsConstructor
    private class User {
        private Long id;
        private String username;
    }
}