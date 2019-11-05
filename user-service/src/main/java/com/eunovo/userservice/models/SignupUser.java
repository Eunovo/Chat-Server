package com.eunovo.userservice.models;

import com.eunovo.userservice.entities.User;

import lombok.Data;

@Data
public class SignupUser {
    private String username;
    private String password;

    public User toUserEntity() {
        return new User(username, password);
    }
}