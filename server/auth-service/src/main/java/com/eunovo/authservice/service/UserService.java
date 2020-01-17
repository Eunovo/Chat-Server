package com.eunovo.authservice.service;

import com.eunovo.authservice.models.JwtRequest;
import com.eunovo.authservice.models.UserResponse;

public interface UserService {

    public UserResponse authenticate(JwtRequest jwtRequest);

}