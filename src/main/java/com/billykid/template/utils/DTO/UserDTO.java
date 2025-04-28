package com.billykid.template.utils.DTO;

import com.billykid.template.entity.DBUser;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor 
public class UserDTO {
    private Integer id;
    private String username;
    private String email;
    private String password;
    private String role;

    // Constructor to map entity to DTO
    public UserDTO(DBUser user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = null;
        this.role = user.getRole().toString();
    }
}