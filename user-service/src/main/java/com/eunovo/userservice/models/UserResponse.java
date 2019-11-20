package com.eunovo.userservice.models;

import com.eunovo.userservice.entities.User;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String username;

    public UserResponse(String username) {
        this.username = username;
    }

    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }
}