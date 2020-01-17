package com.eunovo.authservice.config;

import static com.eunovo.authservice.models.UserResponse.User;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.token.validity}")
    private int tokenValidity;

    public User getUserFromToken(String token) {
        String userIdAsString = getClaimFromToken(token, Claims::getId);
        Long userId = Long.parseLong(userIdAsString);
        String username = getClaimFromToken(token, Claims::getSubject);
        return new User(userId, username);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, user);
    }

    private String doGenerateToken(Map<String, Object> claims, User user) {
        Date currentDateAndTime = new Date(System.currentTimeMillis());
        Date expirationDateAndTime = new Date(System.currentTimeMillis() + tokenValidity * 1000);
        return Jwts.builder().setClaims(claims).setId(user.getId().toString())
                .setSubject(user.getUsername()).setIssuedAt(currentDateAndTime)
                .setExpiration(expirationDateAndTime)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }
}