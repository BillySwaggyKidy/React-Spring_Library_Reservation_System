package com.billykid.template.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Profile;

import com.billykid.template.entity.Author;

@Profile("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthorRepositoryTest {
    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void shouldFindAuthorsByNameIgnoringCase() {
        // GIVEN
        Author author1 = new Author(null, "John Parry", "Writer of bestsellers", LocalDate.of(1985, 1, 1));
        Author author2 = new Author(null, "Joanna Pearl", "Another writer", LocalDate.of(1970, 6, 15));
        Author author3 = new Author(null, "Mike Doe", "Mystery author", LocalDate.of(1990, 3, 20));

        authorRepository.saveAll(List.of(author1, author2, author3));

        // WHEN
        List<Author> results = authorRepository.findByNameContainingIgnoreCase("Jo");

        // THEN
        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(a -> a.getName().equals("John Parry")));
        assertTrue(results.stream().anyMatch(a -> a.getName().equals("Joanna Pearl")));
    }
}
