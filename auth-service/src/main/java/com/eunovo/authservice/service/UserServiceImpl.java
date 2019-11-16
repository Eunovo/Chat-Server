package com.eunovo.authservice.service;

import com.eunovo.authservice.models.JwtRequest;
import com.eunovo.authservice.models.UserResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServiceImpl implements UserService {

    @Value("${userservice.source.url}")
    private String userServiceUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public UserResponse authenticate(JwtRequest jwtRequest) {
        UserResponse userResponse = this.restTemplate.getForObject(userServiceUrl, UserResponse.class);
        return userResponse;
    }

    
}