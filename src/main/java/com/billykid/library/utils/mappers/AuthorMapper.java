package com.billykid.library.utils.mappers;

import org.springframework.stereotype.Component;

import com.billykid.library.entity.Author;
import com.billykid.library.utils.DTO.AuthorDTO;

@Component
public class AuthorMapper {
    public AuthorDTO toDTO(Author author) {
        return new AuthorDTO(author);
    }

    public Author toEntity(AuthorDTO dto) {
        return Author.builder()
            .id(dto.getId())
            .name(dto.getName())
            .bio(dto.getBio())
            .dateOfBirth(dto.getDateOfBirth())
            .build();
    }

    public void updateEntity(Author existingAuthor, AuthorDTO dto) {
        if (dto.getName() != null) {
            existingAuthor.setName(dto.getName());
        }
        if (dto.getBio() != null) {
            existingAuthor.setBio(dto.getBio());
        }
        if (dto.getDateOfBirth() != null) {
            existingAuthor.setDateOfBirth(dto.getDateOfBirth());
        }
    }
}
