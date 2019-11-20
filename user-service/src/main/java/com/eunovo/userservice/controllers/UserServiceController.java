package com.eunovo.userservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.lang.Exception;
import java.util.List;
import java.util.stream.Collectors;

import com.eunovo.userservice.entities.User;
import com.eunovo.userservice.models.*;
import com.eunovo.userservice.services.AddUserService;
import com.eunovo.userservice.services.FindUserService;

@RestController
class UserServiceController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired FindUserService findUserService;
    @Autowired AddUserService addUserService;

    @PostMapping("/authenticate")
    public ApiResponse<UserResponse> authenticateUser(@RequestBody AuthRequest request) throws Exception {
        authenticate(request.getUsername(), request.getPassword());
        User user = findUserService.findByUsername(request.getUsername());
        UserResponse userResponse = new UserResponse(user);
        return ApiResponse.<UserResponse>success("Authenticated", userResponse);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {    
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @GetMapping("/")
    public ApiResponse<List<UserResponse>> getAllUsers() {
        List<User> users = findUserService.findAll();
        users.stream().<UserResponse>map((User user) -> {
            return new UserResponse(user);
        }).collect(Collectors.toList());
        ApiResponse response = ApiResponse.success("All users", users);
        return response;
    }

    @GetMapping("/username/{username}")
    public ApiResponse<UserResponse> getUserByUsername(@PathVariable String username) {
        User user = this.findUserService.findByUsername(username);
        UserResponse userRes = new UserResponse(user);
        return ApiResponse.success("Found 1 User", userRes);
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