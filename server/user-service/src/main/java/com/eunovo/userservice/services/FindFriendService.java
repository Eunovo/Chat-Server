package com.eunovo.userservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.eunovo.userservice.entities.*;
import com.eunovo.userservice.repositories.FriendRepository;

@Service
public class FindFriendService {

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private FindUserService findUserService;

    public List<Friend> getFriendRequests(String username) {
        User user = this.findUserService.findByUsername(username);
        return this.friendRepository.findFriendRequestsForUser(user);
    }

    public List<User> getFriends(String username) {
        User user = this.findUserService.findByUsername(username);
        List<Friend> friends = this.friendRepository.findFriends(user);
        List<User> users = friends.stream().map((Friend friend) -> {
            if (friend.getSource().getUsername() == user.getUsername()) {
                return friend.getTarget();
            }
            return friend.getSource();
        }).collect(Collectors.toList());
        return users;
    }
}