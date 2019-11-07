package com.eunovo.userservice.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Data
@Entity
public class User implements Serializable{
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    private String password;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
}