package com.billykid.template.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.billykid.template.entity.DBUser;
import com.billykid.template.exception.DBUserNotFoundException;
import com.billykid.template.exception.DBUserUserAlreadyExist;

import org.springframework.security.core.userdetails.User;
import com.billykid.template.repository.UserRepository;
import com.billykid.template.utils.DTO.DBUserDTO;
import com.billykid.template.utils.enums.UserRole;
import com.billykid.template.utils.mappers.UserMapper;
import com.billykid.template.utils.parameters.UserParametersObject;
import com.billykid.template.utils.specifications.UserSpecifications;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // Lombok auto-generates the constructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public List<DBUserDTO> findByUsernameContainingIgnoreCase(String username, Pageable pageable) {
        List<DBUser> usersList = userRepository.findByUsernameContainingIgnoreCase(username);
        return usersList.stream().map(DBUserDTO::new).collect(Collectors.toList());
    }

    public List<DBUserDTO> findByEmailContainingIgnoreCase(String email, Pageable pageable) {
        List<DBUser> usersList = userRepository.findByEmailContainingIgnoreCase(email);
        return usersList.stream().map(DBUserDTO::new).collect(Collectors.toList());
    }

    public List<DBUserDTO> findByRole(UserRole role, Pageable pageable) {
        List<DBUser> usersList = userRepository.findByRole(role);
        return usersList.stream().map(DBUserDTO::new).collect(Collectors.toList());
    }

    public List<DBUserDTO> findUsersByQueryParams(UserParametersObject params, Pageable pageable) {
        Specification<DBUser> spec = buildSpecification(params);
        Page<DBUser> userPage = userRepository.findAll(spec, pageable);
        List<DBUser> userList = userPage.getContent();
        return userList.stream().map(DBUserDTO::new).collect(Collectors.toList());
    }

    private Specification<DBUser> buildSpecification(UserParametersObject params) {
        Specification<DBUser> spec = Specification.where(null);

        if (params.getUserName() != null) {
            spec = spec.and(UserSpecifications.hasUsername(params.getUserName()));
        }
        if (params.getEmail() != null) {
            spec = spec.and(UserSpecifications.hasEmail(params.getEmail()));
        }
        if (params.getRole() != null) {
            spec = spec.and(UserSpecifications.hasRole(params.getRole()));
        }

        return spec;
    }

    public DBUserDTO registerUser(DBUserDTO user) {
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

    public DBUserDTO updateUser(Integer id, DBUserDTO user) {
        DBUser existingUser = userRepository.findById(id).orElseThrow(() -> new DBUserNotFoundException("User with ID: " + id + " does not exist"));
        userMapper.updateEntity(existingUser, user);
        userRepository.save(existingUser);
        return userMapper.toDTO(existingUser);
    }

    public DBUserDTO removeUser(Integer id) {
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

