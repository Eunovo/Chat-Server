package com.eunovo.userservice.services;

import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import com.eunovo.userservice.entities.User;
import com.eunovo.userservice.repositories.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FindUserServiceTests {

    @Autowired
    private FindUserService findUserService;

    @BeforeAll
    public static void saveTestUserNovo(@Autowired AddUserService addUserService, @Autowired UserRepository userRepo) {
        userRepo.deleteAll();
        User user = new User("Novo", "password");
        addUserService.addUser(user);
    }

    @Test
    public void shouldFindUserWithUsernamePrefix() {
        String prefix = "N";
        User user = this.findUserService.searchByUsername(prefix);
        assertThat("Result is not null", user, notNullValue());
        assertThat("Result is correct", user.getUsername(), startsWith(prefix));
    }

    @Test
    public void shouldFindUserWithUsernameSuffix() {
        String suffix = "vO";
        User user = this.findUserService.searchByUsername(suffix);
        assertThat("Result is not null", user, notNullValue());
        assertThat("Result is correct", 
            user.getUsername().toLowerCase(), endsWith(suffix.toLowerCase()));
    }
}