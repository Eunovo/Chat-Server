package com.eunovo.authservice.controllers;

import com.eunovo.authservice.config.JwtTokenUtil;
import com.eunovo.authservice.models.*;
import com.eunovo.authservice.service.*;

import static com.eunovo.authservice.models.UserResponse.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil tokenUtil;

    @PostMapping("/generate")
    public JwtResponse<String> generateToken(@RequestBody JwtRequest request) {
        UserResponse response = this.userService.authenticate(request);
        if (response.isSuccessful()) {
            String token = this.tokenUtil.generateToken(response.getData());
            return JwtResponse.<String>success("Token generated", token);
        }
        return JwtResponse.error(response.getMessage());
    }

    @PostMapping("/validate")
    public JwtResponse<User> validateToken(@RequestBody String token) {
        Boolean isValid = this.tokenUtil.validateToken(token);
        if (isValid) {
            User user = this.tokenUtil.getUserFromToken(token);
            return JwtResponse.<User>success("Validated", user);
        }
        return JwtResponse.error("Token is invalid");
    }
}