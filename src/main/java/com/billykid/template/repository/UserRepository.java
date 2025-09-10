package com.billykid.template.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.billykid.template.entity.DBUser;
import com.billykid.template.utils.enums.UserRole;


public interface UserRepository extends JpaRepository<DBUser, Integer>, JpaSpecificationExecutor<DBUser> {
    public Optional<DBUser> findByUsername(String username);
    public List<DBUser> findByUsernameContainingIgnoreCase(String username);
    public List<DBUser> findByEmailContainingIgnoreCase(String email);
    public List<DBUser> findByRole(UserRole role);
    public Optional<DBUser> findByEmail(String email);
}
