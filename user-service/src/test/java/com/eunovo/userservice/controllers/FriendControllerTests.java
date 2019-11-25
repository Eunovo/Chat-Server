package com.eunovo.userservice.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

import com.eunovo.userservice.SecurityServiceTestImpl;
import com.eunovo.userservice.entities.*;
import com.eunovo.userservice.models.*;
import com.eunovo.userservice.repositories.FriendRepository;
import com.eunovo.userservice.services.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FriendControllerTests {

    private final String URL = "/v1/friends";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SecurityServiceTestImpl securityService;

    static final String SOURCE_USERNAME = "Novo";
    static final String TARGET_USERNAME = "Bob";
    static final String SECOND_TARGET_USERNAME = "Ben";
    static User SOURCE_USER;
    static User TARGET_USER;

    @AfterAll
    public static void wipeFriendDbAfterAll(@Autowired FriendRepository friendRepo) {
        friendRepo.deleteAll();
    }

    @BeforeAll
    public static void createTestUsers(@Autowired AddUserService addUserService) {
        User user = new User(SOURCE_USERNAME, "password");
        SOURCE_USER = addUserService.addUser(user);
        user = new User(TARGET_USERNAME, "password");
        TARGET_USER = addUserService.addUser(user);
        user = new User(SECOND_TARGET_USERNAME, "password");
        addUserService.addUser(user);
    }

    @BeforeEach
    public void wipeFriendDb(@Autowired FriendRepository friendRepo) {
        friendRepo.deleteAll();
    }

    @Test
    @WithMockUser(SOURCE_USERNAME)
    public void shouldReturnDefaultMessage() throws Exception {
        this.mockMvc.perform(get(URL + "/test")).andExpect(status().isOk()).andExpect(content().string("Working"));
    }

    @Test
    public void shouldMakeFriendRequest() throws Exception {
        String username = TARGET_USERNAME;
        this.mockMvc.perform(get(URL + "/request/" + username).with(user(SOURCE_USERNAME)))
            .andExpect(status().isOk()).andDo((result) -> {
            String content = result.getResponse().getContentAsString();
            ApiResponse<Friend> response = objectMapper.readValue(content, new TypeReference<ApiResponse<Friend>>() {
            });
            assertEquals("Friend request sent", response.getMessage());
            assertEquals(SOURCE_USERNAME, response.getData().getSource().getUsername());
            assertEquals(TARGET_USERNAME, response.getData().getTarget().getUsername());
        });

        this.mockMvc.perform(get(URL + "/requests").with(user(TARGET_USERNAME)))
            .andExpect(status().isOk()).andDo((result) -> {
            String content = result.getResponse().getContentAsString();
            ApiResponse<List<Friend>> response = objectMapper.readValue(content,
                    new TypeReference<ApiResponse<List<Friend>>>() {
                    });
            assertEquals("Friend requests", response.getMessage());
            Friend friendRequest = response.getData().get(0);
            assertEquals(SOURCE_USERNAME, friendRequest.getSource().getUsername());
            assertEquals(TARGET_USERNAME, friendRequest.getTarget().getUsername());
        });
    }

    @Test
    public void shouldAcceptFriendRequest() throws Exception {
        this.securityService.setLoggedInUser(SOURCE_USER);
        this.mockMvc.perform(get(URL + "/request/" + TARGET_USERNAME))
            .andExpect(status().isOk());

        this.securityService.setLoggedInUser(TARGET_USER);
        this.mockMvc.perform(get(URL + "/accept/" + SOURCE_USERNAME))
            .andExpect(status().isOk())
            .andDo((result) -> {
                String content = result.getResponse().getContentAsString();
                ApiResponse<Friend> response = objectMapper.readValue(content, new TypeReference<ApiResponse<Friend>>() {
                });
                assertEquals("New friend", response.getMessage());
                assertEquals(SOURCE_USERNAME, response.getData().getSource().getUsername());
                assertEquals(TARGET_USERNAME, response.getData().getTarget().getUsername());
            });
        
        this.assertFriends(TARGET_USER, SOURCE_USERNAME, 0);
        this.assertFriends(SOURCE_USER, TARGET_USERNAME, 0);
    }

    private void assertFriends(User user, String username, int index) throws Exception{
        this.securityService.setLoggedInUser(user);
        this.mockMvc.perform(get(URL + "/"))
            .andExpect(status().isOk()).andDo((result) -> {
            String content = result.getResponse().getContentAsString();
            ApiResponse<List<User>> response = objectMapper.readValue(content,
                    new TypeReference<ApiResponse<List<User>>>() {
                    });
            assertEquals("Friends", response.getMessage());
            User friend = response.getData().get(index);
            assertEquals(username, friend.getUsername());
        });
    }

}