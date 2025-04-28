package com.billykid.template.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billykid.template.entity.DBUser;

public interface UserRepository extends JpaRepository<DBUser, Integer> {
    public Optional<DBUser> findByUsername(String username);
    public Optional<DBUser> findByEmail(String email);
}
