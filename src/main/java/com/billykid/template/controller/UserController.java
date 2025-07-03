package com.billykid.template.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.billykid.template.service.CustomUserDetailsService;
import com.billykid.template.utils.DTO.DBUserDTO;
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

    @GetMapping("/users/name/{username}")
    public ResponseEntity<List<DBUserDTO>> getUsersByName(@PathVariable String username, Pageable pageable) {
        List<DBUserDTO> userList = customUserDetailsService.findByUsernameContainingIgnoreCase(username, pageable);
        return ResponseEntity.ok(userList);
    }

    @GetMapping("/users/email/{email}")
    public ResponseEntity<List<DBUserDTO>> getUsersByEmail(@PathVariable String email, Pageable pageable) {
        List<DBUserDTO> userList = customUserDetailsService.findByEmailContainingIgnoreCase(email, pageable);
        return ResponseEntity.ok(userList);
    }

    @GetMapping("/users/role/{role}")
    public ResponseEntity<List<DBUserDTO>> getUsersByRole(@PathVariable UserRole role, Pageable pageable) {
        List<DBUserDTO> userList = customUserDetailsService.findByRole(role, pageable);
        return ResponseEntity.ok(userList);
    }

    @GetMapping("/users")
    public ResponseEntity<List<DBUserDTO>> getUsers(@RequestParam(required=false) String username, @RequestParam(required=false) String email, @RequestParam(required = false) UserRole role, Pageable pageable) {
        UserParametersObject params = UserParametersObject.builder().userName(username).email(email).role(role).build();
        List<DBUserDTO> userList = customUserDetailsService.findUsersByQueryParams(params, pageable);
        return ResponseEntity.ok(userList);
    }

    @PostMapping("/users/add")
    public ResponseEntity<DBUserDTO> addUser(@RequestBody DBUserDTO user) {
        DBUserDTO newUser = customUserDetailsService.registerUser(user);
        return ResponseEntity.ok(newUser);

    }

    @PutMapping("/users/update/{id}")
    public ResponseEntity<DBUserDTO> updateUser(@PathVariable Integer id, @RequestBody DBUserDTO user) {
        DBUserDTO updatedUser = customUserDetailsService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/users/delete/{id}")
    public ResponseEntity<DBUserDTO> deleteUser(@PathVariable Integer id) {
        DBUserDTO removedUser = customUserDetailsService.removeUser(id);
        return ResponseEntity.ok(removedUser);
    }


    
    

    
}
