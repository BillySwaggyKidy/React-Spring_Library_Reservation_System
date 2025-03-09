package com.billykid.template.utils.DTO;

import java.util.Date;

import com.billykid.template.entity.Author;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor 
public class AuthorDTO {
    private Integer id;
    private String name;
    private String bio;
    private Date dateOfBirth;

    // Constructor to map entity to DTO
    public AuthorDTO(Author author) {
        this.id = author.getId();
        this.name = author.getName();
        this.bio = author.getBio();
        this.dateOfBirth = author.getDateOfBirth();
    }
}
