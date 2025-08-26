package com.uade.tpo.marketplace.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.tpo.marketplace.entity.basic.User;


public interface IUserRepository extends JpaRepository<User, Long>{
    // Optional<User> getUserById(Long id);
    // Optional<User> getUserByUsername(String username);
    // Optional<User> getUserByEmail(String email);
    // User creatUser(User user);
    // List<User> getUsers();
    // void deleteUserById(Long id);
}