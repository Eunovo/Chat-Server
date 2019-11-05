package com.eunovo.userservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import com.eunovo.userservice.entities.User;
import com.eunovo.userservice.models.*;
import com.eunovo.userservice.services.AddUserService;
import com.eunovo.userservice.services.FindUserService;

@RestController
class UserServiceController {

    @Autowired FindUserService findUserService;
    @Autowired AddUserService addUserService;

    @GetMapping("/")
    public ApiResponse<List<UserResponse>> getAllUsers() {
        List<User> users = findUserService.findAll();
        users.stream().<UserResponse>map((User user) -> {
            return new UserResponse(user);
        }).collect(Collectors.toList());
        ApiResponse response = ApiResponse.success("All users", users);
        return response;
    }

    @GetMapping("/test")
    public String test() {
        return "Working";
    }

    @PostMapping("/")
    public ApiResponse<UserResponse> addUser(@RequestBody SignupUser user) {
        User newUser = this.addUserService.addUser(user.toUserEntity());
        UserResponse response = new UserResponse(newUser);
        return ApiResponse.<UserResponse>success("User created", response);
    }
}