package com.billykid.template.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Profile;

import com.billykid.template.entity.Author;
import com.billykid.template.repository.AuthorRepository;
import com.billykid.template.utils.DTO.AuthorDTO;
import com.billykid.template.utils.mappers.AuthorMapper;

@Profile("test")
@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {
    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AuthorMapper authorMapper;

    @InjectMocks
    private AuthorService authorService;

    @Test
    void tryFindingAuthorByName() throws Exception {
         // given
        List<Author> authors = List.of(
            new Author(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 )),
			new Author(2,"Johnny Cash","The writer of another best seller", LocalDate.of( 1990 , 10 , 11 ))
        );
        when(authorRepository.findByNameContainingIgnoreCase(eq("John"))).thenReturn(authors);

        // when
        List<AuthorDTO> result = authorService.findAuthorsByName("John", null);

        // then
        assertEquals(2, result.size());
        assertEquals("John Parry", result.get(0).getName());
        assertEquals("Johnny Cash", result.get(1).getName());
    }

    @Test
    void tryAddingNewAuthor() throws Exception {
        Author author = new Author(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 ));
        AuthorDTO authorDTO = new AuthorDTO(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 ));

        when(authorMapper.toEntity(authorDTO)).thenReturn(author);
        when(authorRepository.save(author)).thenReturn(author);
        when(authorMapper.toDTO(author)).thenReturn(authorDTO);

        AuthorDTO result =  authorService.addNewAuthor(authorDTO);

        assertEquals("John Parry", result.getName());
        assertEquals("The writer of the best seller", result.getBio());
    }

    @Test
    void tryUpdatingAuthor() throws Exception {
        Integer id = 1;
        Author existingAuthor = new Author(1, "Old Name", "Old bio", LocalDate.of(1980, 1, 1));
        AuthorDTO incomingDTO = new AuthorDTO(null, "New Name", "New bio", LocalDate.of(1980, 1, 1));
        AuthorDTO expectedDTO = new AuthorDTO(1, "New Name", "New bio", LocalDate.of(1980, 1, 1));

        when(authorRepository.findById(id)).thenReturn(Optional.of(existingAuthor));
        // updateEntity is void — no stubbing needed
        when(authorRepository.save(existingAuthor)).thenReturn(existingAuthor);
        when(authorMapper.toDTO(existingAuthor)).thenReturn(expectedDTO);

        // When
        AuthorDTO result = authorService.updateAuthor(id, incomingDTO);

        // Then
        verify(authorMapper).updateEntity(existingAuthor, incomingDTO); // <- verify the call
        verify(authorRepository).save(existingAuthor);
        assertEquals("New Name", result.getName());
        assertEquals("New bio", result.getBio());
    }

    @Test
    void tryDeletingAuthor() throws Exception {
        // Given
        Integer authorId = 1;
        Author author = new Author();
        author.setId(authorId);
        AuthorDTO expectedDTO = new AuthorDTO();
        expectedDTO.setId(authorId);

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(authorMapper.toDTO(author)).thenReturn(expectedDTO);

        // When
        AuthorDTO result = authorService.removeAuthor(authorId);

        // Then
        verify(authorRepository).findById(authorId);
        verify(authorRepository).delete(author);
        verify(authorMapper).toDTO(author);
        assertEquals(expectedDTO, result);
    }
 }
