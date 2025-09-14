package com.billykid.library.utils.custom;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.billykid.library.entity.DBUser;

public class CustomUserDetails implements UserDetails {
    private final Integer id;
    private final String username;
    private final String password;
    private final Boolean active;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(DBUser user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.active = user.isActive();
        this.authorities = List.of(new SimpleGrantedAuthority(user.getRole().toString()));
    }

    public Integer getId() { return id; }

    @Override
    public String getUsername() { return username; }
    @Override
    public String getPassword() { return password; }
    public Boolean isActive() { return active; }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}