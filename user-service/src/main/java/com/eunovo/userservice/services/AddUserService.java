package com.eunovo.userservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eunovo.userservice.entities.*;
import com.eunovo.userservice.repositories.*;

@Service
public class AddUserService {

    @Autowired UserRepository userRepo;
    
    public User addUser(User user) {
        return userRepo.save(user);
    }
}