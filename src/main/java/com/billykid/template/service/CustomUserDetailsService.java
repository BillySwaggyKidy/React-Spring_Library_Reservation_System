package com.billykid.template.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.billykid.template.entity.DBUser;
import com.billykid.template.exception.DBUserNotFoundException;
import com.billykid.template.exception.DBUserUserAlreadyExist;
import com.billykid.template.exception.ReservationNotFoundException;

import org.springframework.security.core.userdetails.User;
import com.billykid.template.repository.UserRepository;
import com.billykid.template.utils.DTO.UserDTO;
import com.billykid.template.utils.mappers.UserMapper;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // Lombok auto-generates the constructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserDTO registerUser(UserDTO user) {
        if (!userRepository.findByEmail(user.getEmail()).isPresent()) {
            DBUser newUser = userMapper.toEntity(user);
            String encryptedPassword = passwordEncoder.encode(user.getPassword());
            newUser.setPassword(encryptedPassword);
            userRepository.save(newUser);
            return userMapper.toDTO(newUser);
        }
        else {
            throw new DBUserUserAlreadyExist("User already exist");
        }
    }

    public UserDTO updateUser(Integer id, UserDTO user) {
        DBUser existingUser = userRepository.findById(id).orElseThrow(() -> new DBUserNotFoundException("User with ID: " + id + " does not exist"));
        userMapper.updateEntity(existingUser, user);
        userRepository.save(existingUser);
        return userMapper.toDTO(existingUser);
    }

    public UserDTO removeUser(Integer id) {
        DBUser existingUser = userRepository.findById(id).orElseThrow(() -> new DBUserNotFoundException("User with ID: " + id + " does not exist"));
        userRepository.delete(existingUser);
        return userMapper.toDTO(existingUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        DBUser user = userRepository.findByUsername(username).orElseThrow(() -> new DBUserNotFoundException("The user with username: " + username + " not found"));

        return new User(user.getUsername(), user.getPassword(), getGrantedAuthorities(user.getRole().toString()));
    }

    private List<GrantedAuthority> getGrantedAuthorities(String role) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(role));
        return authorities;
    }
}

