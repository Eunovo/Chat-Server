package com.eunovo.userservice.models;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthServiceResponse<T> {
    private String status;
    private String message;
    private T data;
}