package com.eunovo.userservice.services;

import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;


import com.eunovo.userservice.entities.User;
import com.eunovo.userservice.exceptions.*;
import com.eunovo.userservice.models.*;
import com.eunovo.userservice.repositories.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AddUserServiceTests {
    @Autowired
    private AddUserService addUserService;

    @Autowired
    private FindUserService findUserSerivce;

    @Autowired
    private UserRepository userRepo;

    @BeforeEach
    public void wipeDatabase() {
        userRepo.deleteAll();
    }

    @Test
    public void shouldAddUser() {
        SignupUser newUser = new SignupUser("Novo", "password");
        User savedUser = this.addUserService.addUser(newUser.toUserEntity());
        assertEquals(savedUser.getUsername(), newUser.getUsername());
        User result = this.findUserSerivce.findByUsername(newUser.getUsername());
        assertEquals(savedUser, result);
    }

    @Test
    public void shouldRejectUserWithExistingUsername() {
        SignupUser newUserNovo = new SignupUser("Novo", "password");
        this.addUserService.addUser(newUserNovo.toUserEntity());
        SignupUser newUserNovoDuplicate = new SignupUser("Novo", "password");
        assertThrows(IllegalParameterException.class,
                () -> this.addUserService.addUser(newUserNovoDuplicate.toUserEntity()));
    }
}