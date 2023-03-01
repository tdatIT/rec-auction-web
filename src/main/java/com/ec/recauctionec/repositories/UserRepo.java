package com.ec.recauctionec.repositories;

import com.ec.recauctionec.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Integer> {
    @Query(value = "select u from User u where u.username = ?1 and u.password=?2")
    User validateAccount(String username, String password);

    @Query(value = "select u from User u where u.username=?1")
    User findByUsername(String username);

    @Query(value = "select u from User u where u.email = ?1")
    User findByEmail(String email);

    @Query(value = "select u from User u where u.role.roleId <> 1")
    List<User> findAllUserNotAdmin();
}
