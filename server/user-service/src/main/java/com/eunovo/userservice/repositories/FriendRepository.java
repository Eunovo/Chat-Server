package com.eunovo.userservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.eunovo.userservice.entities.*;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    @Query("SELECT f FROM Friend f WHERE f.target = ?1 and f.accepted = false")
    public List<Friend> findFriendRequestsForUser(User user);

    @Query("SELECT f FROM Friend f WHERE f.source = ?1 and f.target = ?2 and f.accepted = false")
    public Friend findFriendRequest(User source, User target);

    @Query("SELECT f FROM Friend f WHERE (f.source = ?1 and f.target = ?2)" + 
        " or (f.source = ?2 and f.target = ?1)")
    public Friend findFriend(User source, User target);

    @Query("SELECT f FROM Friend f WHERE (f.source = ?1 or f.target = ?1)" + 
        " and f.accepted = true")
    public List<Friend> findFriends(User user);

    public void delete(Friend friend);

}