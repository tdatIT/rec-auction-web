package com.ec.recauctionec.data.repositories;

import com.ec.recauctionec.data.entities.User;
import org.springframework.data.domain.Pageable;
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

    @Query("select u from User u order by u.firstName asc")
    List<User> findAllUser(Pageable pageable);

    @Query("select count(u) from User u")
    long totalUserInDb();

    @Query("select count(u) from User u where u.role.roleId = 2")
    long totalSupplierInDb();

    @Query("select count(u) from User u where u.role.roleId = 1")
    long totalAdminInDb();
}
