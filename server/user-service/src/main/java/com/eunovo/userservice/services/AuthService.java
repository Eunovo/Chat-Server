package com.eunovo.userservice.services;

import com.eunovo.userservice.models.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    @Value("${authservice.source.url}")
    private String authServiceUrl;

    @Autowired
    private RestTemplate restTemplate;

    public JwtUser getUserFromToken(String token) {
        String validationUrl = authServiceUrl + "/validate";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        HttpEntity<String> request = new HttpEntity<>(token, headers);
        AuthServiceResponse<JwtUser> response = this.restTemplate.exchange(validationUrl, 
            HttpMethod.POST, 
            request, 
            new ParameterizedTypeReference<AuthServiceResponse<JwtUser>>() {})
            .getBody();
        return response.getData();
    }

}