package com.eunovo.userservice.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

import com.eunovo.userservice.SecurityServiceTestImpl;
import com.eunovo.userservice.entities.*;
import com.eunovo.userservice.models.*;
import com.eunovo.userservice.services.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FriendControllerTests {

    private final String URL = "/v1/friends";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    static final String SOURCE_USERNAME = "Novo";
    static final String TARGET_USERNAME = "Bob";
    static final String SECOND_TARGET_USERNAME = "Ben";
    static User SOURCE_USER;
    static User TARGET_USER;

    @BeforeAll
    public static void createTestUsers(@Autowired AddUserService addUserService,
            @Autowired SecurityServiceTestImpl securityService) {
        User user = new User(SOURCE_USERNAME, "password");
        SOURCE_USER = addUserService.addUser(user);
        user = new User(TARGET_USERNAME, "password");
        TARGET_USER = addUserService.addUser(user);
        user = new User(SECOND_TARGET_USERNAME, "password");
        addUserService.addUser(user);
        securityService.setLoggedInUser(SOURCE_USER);
    }

    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        this.mockMvc.perform(get(URL + "/test")).andExpect(status().isOk()).andExpect(content().string("Working"));
    }

    @Test
    public void shouldMakeFriendRequest() throws Exception {
        String username = TARGET_USERNAME;
        this.mockMvc.perform(get(URL + "/request/" + username)).andDo((result) -> {
            String content = result.getResponse().getContentAsString();
            ApiResponse<Friend> response = objectMapper.readValue(content, new TypeReference<ApiResponse<Friend>>() {
            });
            assertEquals(SOURCE_USERNAME, response.getData().getSource().getUsername());
            assertEquals(TARGET_USERNAME, response.getData().getTarget().getUsername());
        });
    }

}