package com.uade.tpo.marketplace.repository.interfaces;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entity.basic.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Long>{
    // Optional<User> getUserById(Long id);
    // Optional<User> getUserByFirstName(String firstName);
    // Optional<User> getUserByLastName(String lastName);
    // Optional<User> getUserByFullName(String firstName, String lastName);
    // Optional<User> getUserByEmail(String email);
    // User creatUser(User user);
    // List<User> getUsers();
    // void deleteUserById(Long id);
    Optional<User> findByEmail(String email);
}