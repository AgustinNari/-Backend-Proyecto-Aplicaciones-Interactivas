package com.uade.tpo.marketplace.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.tpo.marketplace.entity.basic.User;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IUserRepository extends JpaRepository<User, Long>{
    List<User> getUserById(Long id);
    List<User> getUserByUsername(String username);
    User creatUser(User user);

    
    // Optional<User> getUserByEmail(String email);
    // List<User> getUsers();
    // void deleteUserById(Long id);
}