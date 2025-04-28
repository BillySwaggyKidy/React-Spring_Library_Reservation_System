package com.billykid.template.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.billykid.template.repository.UserRepository;

import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/api")
@RolesAllowed({"ADMIN"})
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    
}
