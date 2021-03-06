package com.eunovo.userservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.eunovo.userservice.entities.*;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAll();

    User findByUsernameIgnoreCase(String username);

    User findByUsernameLikeIgnoreCase(String patter);

    User save(User user);
}