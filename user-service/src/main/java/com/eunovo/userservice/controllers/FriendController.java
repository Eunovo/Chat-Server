package com.eunovo.userservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.eunovo.userservice.models.ApiResponse;
import com.eunovo.userservice.entities.*;
import com.eunovo.userservice.services.*;

import java.util.List;

@RestController
@RequestMapping("/v1/friends")
public class FriendController {

    @Autowired
    private AddFriendService addFriendService;

    @Autowired
    private FindFriendService findFriendService;

    @Autowired
    private SecurityService securityService;

    @GetMapping("/test")
    public String test() {
        return "Working";
    }

    @GetMapping("/")
    public ApiResponse<List<User>> getFriends() {
        String loggedInUsername = this.getCurrentUsername();
        List<User> friends = this.findFriendService
            .getFriends(loggedInUsername);
        return ApiResponse.success("Friends", friends);
    }

    @GetMapping("/request/{username}")
    public ApiResponse<Friend> makeFriendRequest(@PathVariable("username") String username) {
        String loggedInUsername = this.getCurrentUsername();
        Friend friend = this.addFriendService
            .makeFriendRequest(loggedInUsername, username);
        ApiResponse<Friend> response = ApiResponse.success("Friend request sent", friend);
        return response;
    }

    @GetMapping("/requests")
    public ApiResponse<List<Friend>> getFriendRequests() {
        String loggedInUsername = this.getCurrentUsername();
        List<Friend> friendRequests = this.findFriendService.getFriendRequests(loggedInUsername);
        ApiResponse<List<Friend>> response = ApiResponse.success("Friend requests", friendRequests);
        return response;
    }

    @GetMapping("/accept/{username}")
    public ApiResponse<Friend> acceptFriendRequest(
            @PathVariable("username") String username) {
        String loggedInUsername = this.getCurrentUsername();
        Friend friend = this.addFriendService
            .acceptFriendRequest(loggedInUsername, username);
        return ApiResponse.success("New friend", friend);
    }

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        String currentPrincipalName = authentication.getName();
        return currentPrincipalName;
    }
    
}