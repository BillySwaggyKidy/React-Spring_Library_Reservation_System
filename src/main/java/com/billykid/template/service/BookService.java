package com.billykid.template.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.billykid.template.entity.Author;
import com.billykid.template.entity.Book;
import com.billykid.template.entity.BookStatus;
import com.billykid.template.exception.AuthorNotFoundException;
import com.billykid.template.exception.BookNotFoundException;
import com.billykid.template.repository.AuthorRepository;
import com.billykid.template.repository.BookRepository;
import com.billykid.template.utils.DTO.BookDTO;
import com.billykid.template.utils.enums.BookCondition;
import com.billykid.template.utils.mappers.BookMapper;
import com.billykid.template.utils.parameters.BookParametersObject;
import com.billykid.template.utils.specifications.BookSpecifications;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // Lombok auto-generates the constructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    private final BookMapper bookMapper;

    public List<BookDTO> findBooksByTitle(String title, Pageable pageable) {
        List<Book> bookList = bookRepository.findByTitleContainingIgnoreCase(title);
        return bookList.stream().map(BookDTO::new).collect(Collectors.toList());
    }

    public List<BookDTO> findBooksByAuthor(String author, Pageable pageable) {
        List<Book> bookList = bookRepository.findByAuthor_NameContainingIgnoreCase(author);
        return bookList.stream().map(BookDTO::new).collect(Collectors.toList());
    }

    public List<BookDTO> findBooksByGenres(List<String> genres, Pageable pageable) {
        List<Book> bookList = bookRepository.findAll(BookSpecifications.hasGenre(genres));
        return bookList.stream().map(BookDTO::new).collect(Collectors.toList());
    }

    public List<BookDTO> findBooksByAvailable(boolean available, Pageable pageable) {
        List<Book> bookList = bookRepository.findByBookStatus_IsAvailable(!available);
        return bookList.stream().map(BookDTO::new).collect(Collectors.toList());
    }

    public List<BookDTO> findBooksByQueryParams(BookParametersObject params, Pageable pageable) {
        Specification<Book> spec = buildSpecification(params);
        Page<Book> bookPage = bookRepository.findAll(spec, pageable);
        List<Book> bookList = bookPage.getContent();
        return bookList.stream().map(BookDTO::new).collect(Collectors.toList());
    }

    private Specification<Book> buildSpecification(BookParametersObject params) {
        Specification<Book> spec = Specification.where(null);

        if (params.getTitle() != null) {
            spec = spec.and(BookSpecifications.hasTitle(params.getTitle()));
        }
        if (params.getAuthor() != null) {
            spec = spec.and(BookSpecifications.hasAuthor(params.getAuthor()));
        }
        if (params.getGenres() != null) {
            spec = spec.and(BookSpecifications.hasGenre(params.getGenres()));
        }
        if (params.isReserved()) {
            spec = spec.and(BookSpecifications.isReserved(params.isReserved()));
        }

        return spec;
    }

    public BookDTO addNewBook(BookDTO bookDTO) {
        Author author = authorRepository.findById(bookDTO.getAuthorId()).orElseThrow(() -> new AuthorNotFoundException("Author with ID " + bookDTO.getId() + " not found"));
        Book newBook = bookMapper.toEntity(bookDTO, author, null);
        bookRepository.save(newBook);
        BookStatus bookStatus = BookStatus.builder().book(newBook).isAvailable(true).condition(BookCondition.NEW).build();
        newBook.setBookStatus(bookStatus);
        bookRepository.save(newBook);
        return new BookDTO(newBook);
    }

    public BookDTO updateBook(Integer id, BookDTO bookDTO) {
        Book existingBook = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("Book with ID " + id + " not found"));
        Author author =bookDTO.getAuthorId() != null ? authorRepository.findById(bookDTO.getAuthorId()).orElseThrow(() -> new AuthorNotFoundException("Author with ID " + bookDTO.getId() + " not found")) : null;
        bookMapper.updateEntity(existingBook, bookDTO, author);
        bookRepository.save(existingBook);
        return bookMapper.toDTO(existingBook);
    }

    public BookDTO removeBook(Integer id) {
        Book deletedBook = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("Book with ID " + id + " not found"));
        if (deletedBook.getBookStatus() != null) {
            deletedBook.setBookStatus(null); // This will trigger orphan removal
        }    
        bookRepository.delete(deletedBook);
        return bookMapper.toDTO(deletedBook);
    }
    
}
