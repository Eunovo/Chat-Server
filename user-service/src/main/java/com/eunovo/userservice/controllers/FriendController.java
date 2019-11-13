package com.eunovo.userservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.eunovo.userservice.models.ApiResponse;
import com.eunovo.userservice.entities.*;
import com.eunovo.userservice.services.*;

@RestController
@RequestMapping("/v1/friends")
public class FriendController {

    @Autowired
    private AddFriendService addFriendService;

    @GetMapping("/test")
    public String test() {
        return "Working";
    }

    @GetMapping("/request/{username}")
    public ApiResponse<Friend> makeFriendRequest(@PathVariable("username") String username) {
        Friend friend = this.addFriendService.makeFriendRequest("Novo", username);
        ApiResponse<Friend> response = ApiResponse.success("Friend request sent", friend);
        return response;
    }
}