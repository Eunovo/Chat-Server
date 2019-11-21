package com.eunovo.userservice.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
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
    public void shouldAuthenticateUser() throws Exception {
        String username = "Novo";
        String password = "password";
        this.addUser(username, password);
        AuthRequest authRequest = new AuthRequest(username, password);
        String authRequestAsString = this.objectMapper.writeValueAsString(authRequest);
        RequestBuilder authRequestBuilder = post("/authenticate").contentType(MediaType.APPLICATION_JSON)
                .content(authRequestAsString);
        MvcResult result = this.mockMvc.perform(authRequestBuilder).andExpect(status().isOk()).andReturn();
        this.assertUserResult(username, result);
    }

    private void assertUserResult(String username, MvcResult result) throws Exception {
        String response = result.getResponse().getContentAsString();
        ApiResponse<UserResponse> userResponse = this.objectMapper.readValue(response,
                new TypeReference<ApiResponse<UserResponse>>() {
                });
        assertEquals(username, userResponse.getData().getUsername());
    }

    @Test
    public void shouldAddUser() throws Exception {
        String username = "Novo";
        Map<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("username", username);
        requestBody.put("password", "password");
        RequestBuilder request = post("/").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody));
        MvcResult result = this.mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();
        assertUserResult(username, result);

        result = this.mockMvc.perform(get("/")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();
        String responseString = result.getResponse().getContentAsString();
        ApiResponse<List<UserResponse>> users = this.objectMapper.readValue(responseString,
                new TypeReference<ApiResponse<List<UserResponse>>>() {
                });
        assertEquals(users.getData().size(), 1);
        UserResponse user = users.getData().get(0);
        assertEquals(username, user.getUsername());
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

    @Test
    public void shouldRejectUserWithUsedUsernames() throws Exception {
        Map<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("username", "Novo");
        requestBody.put("password", "password");
        List<ApiError> errorsList = new ArrayList();
        errorsList.add(new ApiValidationError("User", "username", "Novo", "already in use"));
        ApiResponse expectedResponse = ApiResponse.error("Illegal parameter", errorsList);
        RequestBuilder request = post("/").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody));

        List<UserResponse> userList = new ArrayList();
        userList.add(new UserResponse("Novo"));
        ApiResponse expectedUsers = ApiResponse.success("All users", userList);

        this.mockMvc.perform(request).andExpect(status().isOk());

        this.mockMvc.perform(request).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));

        this.mockMvc.perform(get("/")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedUsers)));
    }

    @Disabled("TODO")
    @Test
    public void shouldGetAllUsers() {
    }

    @Test
    public void shouldGetUserByUsername() throws Exception {
        String username = "Novo";
        ApiResponse expectedUser = ApiResponse.success("Found 1 User", new UserResponse(username));

        this.mockMvc.perform(get("/username/" + username)).andExpect(status().isNotFound());

        this.addUser(username, "password");

        this.mockMvc.perform(get("/username/" + username)).andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedUser)));
    }

    private void addUser(String username, String password) throws Exception {
        Map<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("username", username);
        requestBody.put("password", password);
        RequestBuilder request = post("/").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody));
        this.mockMvc.perform(request).andExpect(status().isOk());
    }
}