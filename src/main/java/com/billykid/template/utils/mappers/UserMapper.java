package com.billykid.template.utils.mappers;

import org.springframework.stereotype.Component;

import com.billykid.template.entity.DBUser;
import com.billykid.template.utils.DTO.UserDTO;
import com.billykid.template.utils.enums.UserRole;

@Component
public class UserMapper {
    public UserDTO toDTO(DBUser user) {
        return new UserDTO(user);
    }

    public DBUser toEntity(UserDTO dto) {
        return DBUser.builder().username(dto.getUsername()).password(dto.getPassword()).email(dto.getEmail()).role(UserRole.valueOf(dto.getRole())).build();
    }

    public void updateEntity(DBUser existingUser, UserDTO dto) {

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
