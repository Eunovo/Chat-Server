package com.eunovo.userservice.services;

import com.eunovo.userservice.entities.User;
import com.eunovo.userservice.exceptions.ResourceNotFoundException;
import com.eunovo.userservice.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindUserService {
    @Autowired UserRepository userRepo;

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public User findByUsername(String username) {
        User user = userRepo.findByUsernameIgnoreCase(username);
        if (user == null) throw new ResourceNotFoundException("User");
        return user;
    }

    public User searchByUsername(String searchString) {
        User result = userRepo.findByUsernameLikeIgnoreCase("%"+searchString+"%");
        return result;
    }
}