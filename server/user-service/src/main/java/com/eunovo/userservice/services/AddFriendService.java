package com.eunovo.userservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eunovo.userservice.entities.*;
import com.eunovo.userservice.exceptions.IllegalParameterException;
import com.eunovo.userservice.exceptions.ResourceNotFoundException;
import com.eunovo.userservice.repositories.FriendRepository;

@Service
public class AddFriendService {

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private FindUserService findUserService;


    public Friend makeFriendRequest(String sourceUsername, String targetUsername) {
        User sourceUser = this.findUserService.findByUsername(sourceUsername);
        User targetUser = this.findUserService.findByUsername(targetUsername);
        boolean hasDuplicate = this.checkForDuplicatFriend(sourceUser, targetUser);
        if (hasDuplicate) {
            throw new IllegalParameterException("Friend", "target", targetUsername, "already exists");
        }
        Friend friendRequest = new Friend(sourceUser, targetUser);
        return this.friendRepository.save(friendRequest);
    }

    public boolean checkForDuplicatFriend(User source, User target) {
        Friend friend = this.friendRepository.findFriend(source, target);
        if (friend == null) return false;
        return true;
    }

    public Friend acceptFriendRequest(String sourceUsername, String targetUsername) {
        User sourceUser = this.findUserService.findByUsername(sourceUsername);
        User targetUser = this.findUserService.findByUsername(targetUsername);
        Friend friend = this.friendRepository.findFriendRequest(targetUser, sourceUser);
        if (friend == null) throw new ResourceNotFoundException("Friend");
        friend.setAccepted(true);
        return this.friendRepository.save(friend);
    }

    public void declineFriendRequest(String sourceUsername, String targetUsername) {
        User sourceUser = this.findUserService.findByUsername(sourceUsername);
        User targetUser = this.findUserService.findByUsername(targetUsername);
        Friend friend = this.friendRepository.findFriendRequest(targetUser, sourceUser);
        if (friend == null) throw new ResourceNotFoundException("Friend");
        this.friendRepository.delete(friend);
    }
}