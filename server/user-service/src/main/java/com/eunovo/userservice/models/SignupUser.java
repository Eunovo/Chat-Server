package com.eunovo.userservice.models;

import com.eunovo.userservice.entities.User;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupUser {
    private String username;
    private String password;

    public User toUserEntity() {
        return new User(username, password);
    }
}