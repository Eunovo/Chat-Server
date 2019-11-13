package com.eunovo.userservice;

import com.eunovo.userservice.entities.User;
import com.eunovo.userservice.services.SecurityService;

import org.springframework.stereotype.Service;

@Service
public class SecurityServiceTestImpl implements SecurityService {
    
    User loggedInUser = null;

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

}