package com.eunovo.userservice.services;

import com.eunovo.userservice.entities.User;

public interface SecurityService {
    public User getLoggedInUser();
}