package com.eunovo.userservice.models;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtUser {
    private Long id;
    private String username;
}