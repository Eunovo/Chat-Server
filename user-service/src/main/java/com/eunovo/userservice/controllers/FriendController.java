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

    @Autowired
    private SecurityService securityService;

    @GetMapping("/test")
    public String test() {
        return "Working";
    }

    @GetMapping("/request/{username}")
    public ApiResponse<Friend> makeFriendRequest(@PathVariable("username") String username) {
        User loggedInUser = this.securityService.getLoggedInUser();
        Friend friend = this.addFriendService
            .makeFriendRequest(loggedInUser.getUsername(), username);
        ApiResponse<Friend> response = ApiResponse.success("Friend request sent", friend);
        return response;
    }
}