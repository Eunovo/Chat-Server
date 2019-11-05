package com.eunovo.userservice.models;

import com.eunovo.userservice.entities.User;

import lombok.Data;

@Data
public class UserResponse {
    private String username;

    public UserResponse(String username) {
        this.username = username;
    }

    public UserResponse(User user) {
        this.username = user.getUsername();
    }
}