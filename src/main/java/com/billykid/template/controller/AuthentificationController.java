package com.billykid.template.controller;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.billykid.template.service.CustomUserDetailsService;
import com.billykid.template.utils.DTO.DBUserDTO;
import com.billykid.template.utils.parameters.AuthRequest;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/auth")
public class AuthentificationController {
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;

    public AuthentificationController(AuthenticationManager authenticationManager, CustomUserDetailsService customUserDetailsService) {
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
    }

    @GetMapping("/ping")
    public ResponseEntity<Map<String, Object>> ping() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("authenticated", false));
        }

        String username = auth.getName(); // safely get the username
        Collection<? extends GrantedAuthority> roles = auth.getAuthorities();

        return ResponseEntity.ok(Map.of(
            "authenticated", true,
            "username", username,
            "role", roles.stream().map(authority -> authority.getAuthority()).collect(Collectors.toList()).get(0)
        ));
    }
    
    @PostMapping("/signup")
    public ResponseEntity<DBUserDTO> signup(@RequestBody DBUserDTO user, HttpServletRequest request) {
        user.setRole("ROLE_CUSTOMER");
        DBUserDTO newUser = customUserDetailsService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
    

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest loginData, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginData.getUsername(), loginData.getPassword());
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);

        // set the authentication into a new security context
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        // Store the security context in the session so it's reused
        request.getSession(true).setAttribute(
            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context
        );

        return ResponseEntity.ok(Map.of(
            "username", auth.getName(),
            "role", auth.getAuthorities().stream().map(authority -> authority.getAuthority()).collect(Collectors.toList()).get(0)
        ));
    }
    
}
