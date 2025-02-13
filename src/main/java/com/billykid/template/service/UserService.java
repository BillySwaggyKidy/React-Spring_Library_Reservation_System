package com.billykid.template.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.billykid.template.entity.User;
import com.billykid.template.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void saveUser(String name, String email) {
        User n = User.builder().build();
        n.setUsername(name);
        n.setEmail(email);
        userRepository.save(n);
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
    
}
