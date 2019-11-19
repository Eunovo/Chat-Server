package com.eunovo.authservice.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.eunovo.authservice.config.TestConfig;
import com.eunovo.authservice.models.JwtRequest;
import com.eunovo.authservice.models.JwtResponse;
import com.eunovo.authservice.models.UserResponse;

import static com.eunovo.authservice.models.UserResponse.User;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import java.net.URI;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = TestConfig.class)
public class AuthenticationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static MockRestServiceServer mockServer;

    @BeforeAll
    public static void init(@Autowired RestTemplate restTemplate) {
        mockServer = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @BeforeEach
    public void wipeMockServer() {
        mockServer.reset();
    }

    @Test
    public void shouldGenerateToken() throws Exception {
        User user = new User(9L, "Novo");
        UserResponse userResponse = new UserResponse(
            UserResponse.SUCCESS_STATUS, "Authenticated", user
        );
        this.mockAuthenticateRequest(userResponse, HttpStatus.OK);

        RequestBuilder request = this.makeGenerateRequest("Novo", "password");
        this.mockMvc.perform(request).andExpect(status().isOk()).andDo((result) -> {
            String content = result.getResponse().getContentAsString();
            JwtResponse<String> response = this.objectMapper.readValue(
                content, new TypeReference<JwtResponse<String>>() {});
            assertEquals("SUCCESS", response.getStatus());
            assertThat("A token was generated", response.getData().length(), is(not(0)));
        });
    }

    private RequestBuilder makeGenerateRequest(String username, String password) throws Exception{
        JwtRequest jwtRequest = new JwtRequest(username, password);
        String requestBody = this.objectMapper.writeValueAsString(jwtRequest);
        return post("/generate").contentType(MediaType.APPLICATION_JSON).content(requestBody);
    }

    private RequestBuilder makeValidateRequest(String token) throws Exception{
        return post("/validate").contentType(MediaType.TEXT_PLAIN).content(token);
    }

    private void mockAuthenticateRequest(UserResponse userResponse, HttpStatus status) throws Exception {
        String responseContent = this.objectMapper.writeValueAsString(userResponse);
        mockServer.expect(ExpectedCount.once(), 
            requestTo(new URI("http://localhost:4000/authenticate")))
            .andExpect(method(HttpMethod.POST))
            .andRespond(withStatus(status)
            .contentType(MediaType.APPLICATION_JSON)
            .body(responseContent));
    }

    @Test
    public void shouldRejectInvalidUsers() throws Exception {
        String message = "Username or password incorrect";
        UserResponse userResponse = new UserResponse(
            UserResponse.ERROR_STATUS, message, null
        );
        this.mockAuthenticateRequest(userResponse, HttpStatus.OK);

        RequestBuilder request = this.makeGenerateRequest("Novo", "password");
        this.mockMvc.perform(request).andExpect(status().isOk()).andDo((result) -> {
            String content = result.getResponse().getContentAsString();
            JwtResponse<String> response = this.objectMapper.readValue(
                content, new TypeReference<JwtResponse<String>>() {});
            assertEquals("ERROR", response.getStatus());
            assertThat("No token was generated", response.getData(), nullValue());
        });
    }

    @Test
    public void shouldValidateToken() throws Exception {
        String username = "Novo";
        String password = "password";
        User user = new User(9L, username);
        UserResponse userResponse = new UserResponse(
            UserResponse.SUCCESS_STATUS, "Authenticated", user
        );
        this.mockAuthenticateRequest(userResponse, HttpStatus.OK);

        RequestBuilder authRequest = this.makeGenerateRequest(username, password);
        MvcResult authResult = this.mockMvc.perform(authRequest)
                                .andExpect(status().isOk()).andReturn();
        String authContent = authResult.getResponse().getContentAsString();
        JwtResponse<String> authResponse = this.objectMapper.readValue(
                authContent, new TypeReference<JwtResponse<String>>() {});
        String token = authResponse.getData();

        RequestBuilder valRequest = this.makeValidateRequest(token);
        this.mockMvc.perform(valRequest).andExpect(status().isOk()).andDo((result) -> {
            String content = result.getResponse().getContentAsString();
            JwtResponse<String> response = this.objectMapper.readValue(
                content, new TypeReference<JwtResponse<String>>() {});
            assertEquals("SUCCESS", response.getStatus());
            assertThat("A token was generated", response.getData().length(), is(not(0)));
        });
    }

    @Test
    public void shouldRejectExpiredToken() {

    }
}