package com.billykid.library.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.billykid.library.service.CustomUserDetailsService;
import com.billykid.library.utils.DTO.DBUserDTO;
import com.billykid.library.utils.DTO.PingResponseDTO;
import com.billykid.library.utils.custom.CustomUserDetails;
import com.billykid.library.utils.parameters.AuthRequest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



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
    public ResponseEntity<PingResponseDTO> ping() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal() == "anonymousUser") {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new PingResponseDTO(false, null,null,null));
        }

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        PingResponseDTO response = new PingResponseDTO(
            true,
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getAuthorities().iterator().next().getAuthority()
        );
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/signup")
    public ResponseEntity<DBUserDTO> signup(@RequestBody DBUserDTO user, HttpServletRequest request) {
        user.setRole("ROLE_CUSTOMER");
        DBUserDTO newUser = customUserDetailsService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
    

    @PostMapping("/login")
    public ResponseEntity<PingResponseDTO> login(@RequestBody AuthRequest loginData, HttpServletRequest request) {
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

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        if (!userDetails.isActive()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new PingResponseDTO(false, null,null,null));
        }

        PingResponseDTO response = new PingResponseDTO(
            true,
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getAuthorities().iterator().next().getAuthority()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/account/{id}")
    @RolesAllowed({"CUSTOMER", "EMPLOYEE", "ADMIN"})
    public ResponseEntity<DBUserDTO> changeAccountInfo(@PathVariable Integer id, @RequestBody DBUserDTO user) {
        DBUserDTO updatedUser = customUserDetailsService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }
    
}
