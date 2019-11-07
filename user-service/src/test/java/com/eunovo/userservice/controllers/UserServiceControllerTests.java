package com.eunovo.userservice.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.eunovo.userservice.models.*;
import com.eunovo.userservice.repositories.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepo;

    @BeforeEach
    public void wipeDatabase() {
        userRepo.deleteAll();
    }

    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        this.mockMvc.perform(get("/test")).andExpect(status().isOk()).andExpect(content().string("Working"));
    }

    @Test
    public void shouldAddUser() throws Exception {
        Map<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("username", "Novo");
        requestBody.put("password", "password");
        ApiResponse expectedResponse = ApiResponse.success("User created", new UserResponse("Novo"));
        RequestBuilder request = post("/").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody));
        List<UserResponse> userList = new ArrayList();
        userList.add(new UserResponse("Novo"));
        ApiResponse expectedUsers = ApiResponse.success("All users", userList);

        this.mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));

        this.mockMvc.perform(get("/")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedUsers)));
    }

    @Test
    public void shouldRejectInvalidUser() throws Exception {
        Map<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("username", "Novo");
        List<ApiError> errorsList = new ArrayList();
        errorsList.add(new ApiValidationError("User", "password", null, "must not be null"));
        ApiResponse expectedResponse = ApiResponse.error("Validation error", errorsList);
        RequestBuilder request = post("/").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody));
        List<UserResponse> userList = new ArrayList();
        ApiResponse expectedUsers = ApiResponse.success("All users", userList);

        this.mockMvc.perform(request).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));

        this.mockMvc.perform(get("/")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedUsers)));
    }

    @Disabled("TODO")
    @Test
    public void shouldRejectUserWithUsedUsernames() {
    }

    @Disabled("TODO")
    @Test
    public void shouldGetAllUsers() {
    }

    @Disabled("TODO")
    @Test
    public void shouldGetUserById() {
    }
}