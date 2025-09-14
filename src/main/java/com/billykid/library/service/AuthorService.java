package com.billykid.library.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.billykid.library.entity.Author;
import com.billykid.library.exception.AuthorNotFoundException;
import com.billykid.library.repository.AuthorRepository;
import com.billykid.library.utils.DTO.AuthorDTO;
import com.billykid.library.utils.mappers.AuthorMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // Lombok auto-generates the constructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;
    
    public List<AuthorDTO> findAuthorsByName(String name, Pageable pageable) {
        List<Author> authorList = authorRepository.findByNameContainingIgnoreCase(name);
        return authorList.stream().map(AuthorDTO::new).collect(Collectors.toList());
    }

    public AuthorDTO addNewAuthor(AuthorDTO authorDTO) {
        Author author = authorMapper.toEntity(authorDTO);
        authorRepository.save(author);
        return authorMapper.toDTO(author);
    }

    public AuthorDTO updateAuthor(Integer id, AuthorDTO authorDTO) {
        Author existingAuthor = authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException("Author with ID " + id + " not found"));
        authorMapper.updateEntity(existingAuthor, authorDTO);
        authorRepository.save(existingAuthor);
        return authorMapper.toDTO(existingAuthor);
    }

    public AuthorDTO removeAuthor(Integer id) {
        Author deletedAuthor = authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException("Author with ID " + id + " not found"));
        authorRepository.delete(deletedAuthor);
        return authorMapper.toDTO(deletedAuthor);
    }

    


    
}