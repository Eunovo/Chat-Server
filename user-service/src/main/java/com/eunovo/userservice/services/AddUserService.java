package com.eunovo.userservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eunovo.userservice.entities.*;
import com.eunovo.userservice.exceptions.*;
import com.eunovo.userservice.repositories.*;

@Service
public class AddUserService {

    @Autowired
    UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User addUser(User user) {
        this.ensureUsernameIsUnique(user);
        String encodedPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepo.save(user);
    }

    private void ensureUsernameIsUnique(User user) {
        User duplicateUser = this.userRepo.findByUsernameIgnoreCase(user.getUsername());
        if (duplicateUser == null)
            return;
        throw new IllegalParameterException("User", "username", user.getUsername(), "already in use");
    }
}