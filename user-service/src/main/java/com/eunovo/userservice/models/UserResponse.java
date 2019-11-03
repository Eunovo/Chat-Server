package com.eunovo.userservice.models;

import lombok.Data;

@Data
public class UserResponse {
    private String username;

    public UserResponse(String username) {
        this.username = username;
    }
}