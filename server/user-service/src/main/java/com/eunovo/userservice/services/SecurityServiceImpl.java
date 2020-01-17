package com.eunovo.userservice.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

    public String getLoggedInUsername() {
        Authentication authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        String currentPrincipalName = authentication.getName();
        return currentPrincipalName;
    }
}