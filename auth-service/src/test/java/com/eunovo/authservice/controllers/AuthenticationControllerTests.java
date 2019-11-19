package com.eunovo.authservice.controllers;

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

    @Test
    public void shouldGenerateToken() throws Exception {
        User user = new User(9L, "Novo");
        UserResponse userResponse = new UserResponse("SUCCESS", "Authenticated", user);
        this.mockAuthenticateRequest(userResponse, HttpStatus.OK);

        JwtRequest jwtRequest = new JwtRequest("Novo", "password");
        String requestBody = this.objectMapper.writeValueAsString(jwtRequest);
        RequestBuilder request = post("/generate").contentType(MediaType.APPLICATION_JSON).content(requestBody);
        this.mockMvc.perform(request).andExpect(status().isOk()).andDo((result) -> {
            String content = result.getResponse().getContentAsString();
            JwtResponse response = this.objectMapper.readValue(content, JwtResponse.class);
            assertEquals("SUCCESS", response.getStatus());
            assertThat("A token was generated", response.getToken().length(), is(not(0)));
        });
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
    public void shouldRejectInvalidUsers() {

    }

    @Test
    public void shouldValidateToken() {

    }

    @Test
    public void shouldRejectExpiredToken() {

    }
}