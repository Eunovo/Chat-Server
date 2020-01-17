package com.eunovo.userservice.services;

import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import com.eunovo.userservice.entities.*;
import com.eunovo.userservice.exceptions.*;
import com.eunovo.userservice.repositories.FriendRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AddFriendServiceTests {

    @Autowired
    private AddFriendService addFriendService;

    @Autowired
    private FindFriendService findFriendService;

    @Autowired
    private FriendRepository friendRepository;

    static final String SOURCE_USERNAME = "Novo";
    static final String TARGET_USERNAME = "Bob";
    static final String SECOND_TARGET_USERNAME = "Ben";
    @BeforeAll
    public static void createTestUsers(@Autowired AddUserService addUserService) {
        User user = new User(SOURCE_USERNAME, "password");
        addUserService.addUser(user);
        user = new User(TARGET_USERNAME, "password");
        addUserService.addUser(user);
        user = new User(SECOND_TARGET_USERNAME, "password");
        addUserService.addUser(user);
    }

    @AfterAll
    public static void wipeFriendDbAfterAll(@Autowired FriendRepository friendRepo) {
        friendRepo.deleteAll();
    }

    @BeforeEach
    public void clearFriendsDb() {
        this.friendRepository.deleteAll();
    }

    @Test
    public void shoulMakeFriendRequest() {
        String me = SOURCE_USERNAME;
        String targetFriend = TARGET_USERNAME;
        String secondTarget = SECOND_TARGET_USERNAME;
        this.assertSuccessfulFriendRequest(me, targetFriend);
        this.assertSuccessfulFriendRequest(me, secondTarget);
    }

    public void assertSuccessfulFriendRequest(String sourceUsername, String targetUsername) {
        Friend friendRequest = this.addFriendService.makeFriendRequest(sourceUsername, targetUsername);
        assertThat("Friend request is not accepted", friendRequest.getAccepted(), is(false));
        List<Friend> friendRequests = this.findFriendService.getFriendRequests(targetUsername);
        List<Friend> expectedRequests = List.of(friendRequest);
        assertEquals(expectedRequests, friendRequests);
    }

    @Test
    public void shouldRejectDuplicateFriendRequest() {
        String me = SOURCE_USERNAME;
        String targetFriend = TARGET_USERNAME;
        this.addFriendService.makeFriendRequest(me, targetFriend);
        assertThrows(IllegalParameterException.class, 
            () -> this.addFriendService.makeFriendRequest(me, targetFriend));
    }

    @Test
    public void shouldRejectFriendRequestToAnExistingFriend() {
        String me = SOURCE_USERNAME;
        String targetFriend = TARGET_USERNAME;
        this.addFriendService.makeFriendRequest(me, targetFriend);
        this.addFriendService.acceptFriendRequest(targetFriend, me);
        assertThrows(IllegalParameterException.class, 
            () -> this.addFriendService.makeFriendRequest(me, targetFriend));
        assertThrows(IllegalParameterException.class, 
            () -> this.addFriendService.makeFriendRequest(targetFriend, me));
    }

    @Test
    public void shouldAcceptFriendRequest() {
        String me = SOURCE_USERNAME;
        String targetFriend = TARGET_USERNAME;
        this.addFriendService.makeFriendRequest(me, targetFriend);
        Friend friend = this.addFriendService.acceptFriendRequest(targetFriend, me);
        List<User> myFriends = this.findFriendService.getFriends(me);
        assertEquals(friend.getTarget(), myFriends.get(0));
        List<User> targetFriends = this.findFriendService.getFriends(targetFriend);
        assertEquals(friend.getSource(), targetFriends.get(0));
    }

    @Test
    public void shouldNotAcceptNonExistingFriendRequest() {
        String me = SOURCE_USERNAME;
        String targetFriend = TARGET_USERNAME;
        assertThrows(ResourceNotFoundException.class, 
            () -> this.addFriendService
                .acceptFriendRequest(targetFriend, me));
    }

    @Test
    public void shouldDeclineFriendRequest() {
        String me = SOURCE_USERNAME;
        String targetFriend = TARGET_USERNAME;
        Friend request = this.addFriendService.makeFriendRequest(me, targetFriend);
        List<Friend> requests = this.findFriendService.getFriendRequests(targetFriend);
        assertEquals(request, requests.get(0));
        this.addFriendService.declineFriendRequest(targetFriend, me);
        requests = this.findFriendService.getFriendRequests(targetFriend);
        assertEquals(0, requests.size());
    }

    @Disabled
    @Test
    public void shouldRemoveFriend() {}
}