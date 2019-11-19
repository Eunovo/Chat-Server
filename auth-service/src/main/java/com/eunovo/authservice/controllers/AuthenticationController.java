package com.eunovo.authservice.controllers;

import com.eunovo.authservice.config.JwtTokenUtil;
import com.eunovo.authservice.models.*;
import com.eunovo.authservice.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil tokenUtil;

    @PostMapping("/generate")
    public JwtResponse generateToken(JwtRequest request) {
        UserResponse response = this.userService.authenticate(request);
        if (response.isSuccessful()) {
            String token = this.tokenUtil.generateToken(response.getData());
            return JwtResponse.success("Token generated", token);
        }
        return JwtResponse.error(response.getMessage());
    }
}