package com.billykid.library.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.billykid.library.entity.DBUser;
import com.billykid.library.exception.DBUserNotFoundException;
import com.billykid.library.exception.DBUserUserAlreadyExist;
import com.billykid.library.repository.UserRepository;
import com.billykid.library.utils.DTO.DBUserDTO;
import com.billykid.library.utils.DTO.PagedResponse;
import com.billykid.library.utils.custom.CustomUserDetails;
import com.billykid.library.utils.enums.UserRole;
import com.billykid.library.utils.mappers.UserMapper;
import com.billykid.library.utils.parameters.UserParametersObject;
import com.billykid.library.utils.specifications.UserSpecifications;

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

    public DBUserDTO getUserDetailsById(Integer id) {
        DBUser existingUser = userRepository.findById(id).orElseThrow(() -> new DBUserNotFoundException("User with ID: " + id + " does not exist"));
        return userMapper.toDTO(existingUser);
    }

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

    public PagedResponse<DBUserDTO> findUsersByQueryParams(UserParametersObject params, Pageable pageable) {
        Specification<DBUser> spec = buildSpecification(params);
        Page<DBUser> userPage = userRepository.findAll(spec, pageable);
        List<DBUserDTO> userList = userPage.getContent().stream().map(DBUserDTO::new).collect(Collectors.toList());
        return new PagedResponse<DBUserDTO>(
            userList,
            userPage.getNumber(),
            userPage.getSize(),
            userPage.getTotalElements(),
            userPage.getTotalPages()
        );
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
        spec = spec.and(UserSpecifications.isActive());

        return spec;
    }

    public DBUserDTO registerUser(DBUserDTO user) {
        if (userRepository.findByEmailAndActiveTrue(user.getEmail()).isPresent()) {
            throw new DBUserUserAlreadyExist("Email already in use");
        }

        if (userRepository.findByUsernameAndActiveTrue(user.getUsername()).isPresent()) {
            throw new DBUserUserAlreadyExist("Username already in use");
        }

        DBUser newUser = userMapper.toEntity(user);
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        newUser.setPassword(encryptedPassword);
        newUser.setActive(true);
        userRepository.save(newUser);
        return userMapper.toDTO(newUser);
    }

    public DBUserDTO updateUser(Integer id, DBUserDTO user) {
        DBUser existingUser = userRepository.findById(id).orElseThrow(() -> new DBUserNotFoundException("User with ID: " + id + " does not exist"));
        if (user.getPassword() != null) {
            String encryptedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);
        }
        userMapper.updateEntity(existingUser, user);
        userRepository.save(existingUser);
        return userMapper.toDTO(existingUser);
    }

    public DBUserDTO softDeleteUser(Integer id) {
        DBUser existingUser = userRepository.findById(id).orElseThrow(() -> new DBUserNotFoundException("User with ID: " + id + " does not exist"));
        existingUser.setActive(false);
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

        return new CustomUserDetails(user);
    }

    private List<GrantedAuthority> getGrantedAuthorities(String role) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(role));
        return authorities;
    }
}

