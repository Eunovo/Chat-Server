package com.eunovo.userservice.security;

import com.eunovo.userservice.entities.User;
import com.eunovo.userservice.models.*;
import com.eunovo.userservice.services.AddUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.eunovo.userservice.config.TestConfig;

import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(TestConfig.class)
@AutoConfigureMockMvc
public class AuthenticationTests {

    @Value("${authservice.source.url}")
    private String authServiceUrl;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AddUserService addUserService;

    private final String userSerivceUrl = "";

    private static MockRestServiceServer mockServer;

    @BeforeAll
    public static void initMockServer(@Autowired RestTemplate restTemplate) {
        mockServer = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @BeforeEach
    public void wipeMockServer() {
        mockServer.reset();
    }

    @Test
    public void shouldValidateToken() throws Exception {
        Long id = 9L;
        String username = "Novo";
        User user = new User(username, "password");
        this.addUserService.addUser(user);
        String url = userSerivceUrl+"/v1/friends/test";
        RequestBuilder request = get(url);
        this.assertIsUnAuthenticated(request);

        String token = "token";
        String bearerToken = "Bearer " + token;
        request = get(url).header("Authorization", bearerToken);
        JwtUser jwtUser = new JwtUser(id, username);
        this.mockValidationRequest(token, jwtUser, HttpStatus.OK);
        this.assertIsAuthenticated(request);
        mockServer.verify();
    }

    private void mockValidationRequest(String expectedToken, JwtUser user, HttpStatus status) throws Exception {
        AuthServiceResponse<JwtUser> response = 
            new AuthServiceResponse<>("SUCCESS", "Validated", user);
        String responseContent = this.objectMapper.writeValueAsString(response);
        mockServer.expect(ExpectedCount.once(), 
            requestTo(new URI(authServiceUrl+"/validate")))
            .andExpect(method(HttpMethod.POST))
            .andExpect(content().string(expectedToken))
            .andRespond(withStatus(status)
            .contentType(MediaType.APPLICATION_JSON)
            .body(responseContent));
    }

    private void assertIsUnAuthenticated(RequestBuilder request) throws Exception {
        this.mockMvc.perform(request).andExpect(status().isForbidden());
    }

    private void assertIsAuthenticated(RequestBuilder request) throws Exception {
        this.mockMvc.perform(request).andExpect(status().isOk());
    }
}