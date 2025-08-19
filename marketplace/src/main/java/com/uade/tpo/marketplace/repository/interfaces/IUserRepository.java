package com.uade.tpo.marketplace.repository.interfaces;

import com.uade.tpo.marketplace.entity.basic.User;
import java.util.Optional;
import java.util.List;

public interface IUserRepository {
    Optional<User> getUserById(Integer id);
    Optional<User> getUserByUsername(String username);
    Optional<User> getUserByEmail(String email);
    User creatUser(User user);
    List<User> getUsers();
    void deleteUserById(Integer id);
}