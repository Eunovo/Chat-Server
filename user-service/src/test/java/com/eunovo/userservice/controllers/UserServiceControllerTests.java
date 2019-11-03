package com.eunovo.userservice.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.eunovo.userservice.models.*;

@RunWith(SpringRunner.class)
@WebMvcTest
public class UserServiceControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private final String SUCCESS_MESSAGE = "SUCCESS";

    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk()).andExpect(content().string("Working"));
    }

    @Test
    public void shouldAddUser() throws Exception {
        Map<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("username", "Novo");
        requestBody.put("password", "password");
        ApiResponse expectedResponse = ApiResponse.success("User created", new UserResponse("Novo"));
        RequestBuilder request = post("/").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody));
        this.mockMvc.perform(request).andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }
    
}