package com.uade.tpo.marketplace.repository.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.tpo.marketplace.entity.basic.User;


public interface IUserRepository extends JpaRepository<User, Integer> {
    Optional<User> getUserById(Integer id);
    Optional<User> getUserByUsername(String username);
    Optional<User> getUserByEmail(String email);
    User creatUser(User user);
    List<User> getUsers();
    void deleteUserById(Integer id);
}