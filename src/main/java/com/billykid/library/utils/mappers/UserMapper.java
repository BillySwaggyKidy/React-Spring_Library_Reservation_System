package com.billykid.library.utils.mappers;

import org.springframework.stereotype.Component;

import com.billykid.library.entity.DBUser;
import com.billykid.library.utils.DTO.DBUserDTO;
import com.billykid.library.utils.enums.UserRole;

@Component
public class UserMapper {
    public DBUserDTO toDTO(DBUser user) {
        return new DBUserDTO(user);
    }

    public DBUser toEntity(DBUserDTO dto) {
        return DBUser.builder().username(dto.getUsername()).password(dto.getPassword()).email(dto.getEmail()).role(UserRole.valueOf(dto.getRole())).build();
    }

    public void updateEntity(DBUser existingUser, DBUserDTO dto) {

        if (dto.getUsername() != null) {
            existingUser.setUsername(dto.getUsername());
        }
        if (dto.getPassword() != null) {
            existingUser.setPassword(dto.getPassword());
        }
        if (dto.getEmail() != null) {
            existingUser.setEmail(dto.getEmail());
        }
        if (dto.getRole() != null) {
            existingUser.setRole(UserRole.valueOf(dto.getRole()));
        }
    }
}
