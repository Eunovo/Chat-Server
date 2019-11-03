package com.eunovo.userservice.controllers;

import org.springframework.web.bind.annotation.*;

import com.eunovo.userservice.models.*;

@RestController
class UserServiceController {

    @GetMapping("/")
    public String index() {
        return "Working";
    }

    @PostMapping("/")
    public ApiResponse<UserResponse> addUser(@RequestBody SignupUser user) {
        UserResponse response = new UserResponse(user.getUsername());
        return ApiResponse.<UserResponse>success("User created", response);
    }
}