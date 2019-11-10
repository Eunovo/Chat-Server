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
import com.eunovo.userservice.exceptions.IllegalParameterException;
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
    @BeforeAll
    public static void createTestUsers(@Autowired AddUserService addUserService) {
        User user = new User(SOURCE_USERNAME, "password");
        addUserService.addUser(user);
        user = new User(TARGET_USERNAME, "password");
        addUserService.addUser(user);
    }

    @BeforeEach
    public void clearFriendsDb() {
        this.friendRepository.deleteAll();
    }

    @Test
    public void shoulMakeFriendRequest() {
        String me = SOURCE_USERNAME;
        String targetFriend = TARGET_USERNAME;
        Friend friendRequest = this.addFriendService.makeFriendRequest(me, targetFriend);
        assertThat("Friend request is not accepted", friendRequest.getAccepted(), is(false));
        List<Friend> friendRequests = this.findFriendService.getFriendRequests(me);
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

    @Disabled
    @Test
    public void shouldRejectFriendRequestToAnExistingFriend() {}

    @Disabled
    @Test
    public void shouldAcceptFriendRequest() {}

    @Disabled
    @Test
    public void shouldRejectFriendRequest() {}

    @Disabled
    @Test
    public void shouldRemoveFriend() {}
}