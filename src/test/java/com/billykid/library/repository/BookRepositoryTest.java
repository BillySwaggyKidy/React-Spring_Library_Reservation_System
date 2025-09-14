package com.billykid.library.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Profile;

import com.billykid.library.entity.Author;
import com.billykid.library.entity.Book;
import com.billykid.library.entity.BookStatus;
import com.billykid.library.repository.BookRepository;
import com.billykid.library.utils.enums.BookQuality;

@Profile("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Author author;

    @BeforeEach
    void setup() {
        author = Author.builder()
        .name("John Parry")
        .bio("Best selling author")
        .dateOfBirth(LocalDate.of(1985, 1, 1))
        .build();
        entityManager.persist(author);

        Book book1 = Book.builder()
        .title("Captain underpants")
        .author(author)
        .build();

        BookStatus status1 = new BookStatus();
        status1.setAvailable(true);
        status1.setQuality(BookQuality.NEW);
        status1.setBook(book1);
        book1.setBookStatus(status1);

        entityManager.persist(book1);
        // Pas besoin de persist(status1) si cascade sur bookStatus

        Book book2 = Book.builder()
        .title("The Adventures of Larry")
        .author(author)
        .build();

        BookStatus status2 = new BookStatus();
        status2.setAvailable(false);
        status2.setQuality(BookQuality.NEW);
        status2.setBook(book2);
        book2.setBookStatus(status2);

        entityManager.persist(book2);

        entityManager.flush();
    }

    @Test
    void testFindByTitleContainingIgnoreCase() {
        List<Book> books = bookRepository.findByTitleContainingIgnoreCase("captain");
        assertEquals(1, books.size());
        assertEquals("Captain underpants", books.get(0).getTitle());
    }

    @Test
    void testFindByAuthorNameContainingIgnoreCase() {
        List<Book> books = bookRepository.findByAuthor_NameContainingIgnoreCase("parry");
        assertEquals(2, books.size());
    }

    @Test
    void testFindByBookStatusIsAvailable() {
        List<Book> availableBooks = bookRepository.findByBookStatus_IsAvailable(true);
        assertEquals(1, availableBooks.size());
        assertTrue(availableBooks.get(0).getBookStatus().isAvailable());

        List<Book> unavailableBooks = bookRepository.findByBookStatus_IsAvailable(false);
        assertEquals(1, unavailableBooks.size());
        assertFalse(unavailableBooks.get(0).getBookStatus().isAvailable());
    }
}
