package com.billykid.template.controller;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.billykid.template.service.CustomUserDetailsService;
import com.billykid.template.utils.DTO.UserDTO;
import com.billykid.template.utils.parameters.AuthRequest;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
public class AuthentificationController {
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;

    public AuthentificationController(AuthenticationManager authenticationManager, CustomUserDetailsService customUserDetailsService) {
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signup(@RequestBody UserDTO user, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean hasAdminRole = auth.getAuthorities().stream().map(authority -> authority.getAuthority()).anyMatch(role -> role.equals("ROLE_ADMIN"));
        if (!hasAdminRole) {
            user.setRole("ROLE_CUSTOMER");
        }
        UserDTO newUser = customUserDetailsService.registerUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
    

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest loginData, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginData.getUsername(), loginData.getPassword());
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);

        request.getSession();

        return ResponseEntity.ok(Map.of(
            "username", auth.getName(),
            "role", auth.getAuthorities().stream().map(authority -> authority.getAuthority()).collect(Collectors.toList()).get(0)
        ));
    }
    
}
