package com.billykid.template.utils.DTO;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PingResponseDTO {
    private Boolean authenticated;
    private Integer id;
    private String username;
    private String role;

    public PingResponseDTO(Boolean authenticated, Integer id, String username, String role) {
        this.authenticated = authenticated;
        this.id = id;
        this.username = username;
        this.role = role;
    }
}
