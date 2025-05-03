package com.billykid.template.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.billykid.template.service.CustomUserDetailsService;
import com.billykid.template.utils.DTO.UserDTO;
import com.billykid.template.utils.enums.UserRole;
import com.billykid.template.utils.parameters.UserParametersObject;

import jakarta.annotation.security.RolesAllowed;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api")
@RolesAllowed({"ADMIN"})
public class UserController {
    private final CustomUserDetailsService customUserDetailsService;

    public UserController(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @GetMapping("/users/username/{username}")
    public ResponseEntity<List<UserDTO>> getUsersByName(@PathVariable String username) {
        List<UserDTO> userList = customUserDetailsService.findByUsernameContainingIgnoreCase(username);
        return ResponseEntity.ok(userList);
    }

    @GetMapping("/users/email/{email}")
    public ResponseEntity<List<UserDTO>> getUsersByEmail(@PathVariable String email) {
        List<UserDTO> userList = customUserDetailsService.findByEmailContainingIgnoreCase(email);
        return ResponseEntity.ok(userList);
    }

    @GetMapping("/users/role/{role}")
    public ResponseEntity<List<UserDTO>> getUsersByRole(@PathVariable UserRole role) {
        List<UserDTO> userList = customUserDetailsService.findByRole(role);
        return ResponseEntity.ok(userList);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getUsers(@RequestParam(required=false) String username, @RequestParam(required=false) String email, @RequestParam(required = false) UserRole role, Pageable pageable) {
        UserParametersObject params = UserParametersObject.builder().userName(username).email(email).role(role).build();
        List<UserDTO> userList = customUserDetailsService.findUsersByQueryParams(params, pageable);
        return ResponseEntity.ok(userList);
    }

    @PostMapping("/users/add")
    public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO user) {
        UserDTO newUser = customUserDetailsService.registerUser(user);
        return ResponseEntity.ok(newUser);

    }

    @PutMapping("/users/update/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Integer id, @RequestBody UserDTO user) {
        UserDTO updatedUser = customUserDetailsService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/users/delete/{id}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable Integer id) {
        UserDTO removedUser = customUserDetailsService.removeUser(id);
        return ResponseEntity.ok(removedUser);
    }


    
    

    
}
