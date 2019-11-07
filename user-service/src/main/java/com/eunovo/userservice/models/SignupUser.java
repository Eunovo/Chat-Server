package com.eunovo.userservice.models;

import com.eunovo.userservice.entities.User;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupUser {
    private String username;
    private String password;

    public User toUserEntity() {
        return new User(username, password);
    }
}