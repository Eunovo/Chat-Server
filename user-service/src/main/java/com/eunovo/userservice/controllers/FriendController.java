package com.eunovo.userservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/friends")
public class FriendController {

    @GetMapping("/test")
    public String test() {
        return "Working";
    }
}