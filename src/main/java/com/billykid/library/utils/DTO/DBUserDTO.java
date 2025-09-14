package com.billykid.library.utils.DTO;

import com.billykid.library.entity.DBUser;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DBUserDTO {
    private Integer id;
    private String username;
    private String email;
    private String password;
    private String role;

    public DBUserDTO(Integer id, String username, String email, String password, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Constructor to map entity to DTO
    public DBUserDTO(DBUser user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = null;
        this.role = user.getRole().toString();
    }
}