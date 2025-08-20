package com.uade.tpo.marketplace.service;

import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entity.basic.User;
import com.uade.tpo.marketplace.entity.dto.AuthRegisterDto;
import com.uade.tpo.marketplace.entity.dto.AuthResponseDto;
import com.uade.tpo.marketplace.exceptions.ResourceNotFoundException;
import com.uade.tpo.marketplace.repository.interfaces.IUserRepository;

@Service
public class UserService {
    
    private IUserRepository userRepository;

   
    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(Integer id) {
        return userRepository.getUserById(id).orElseThrow(() -> new ResourceNotFoundException("El usuario no fue encontrado: " + id));
    }

}
