package com.billykid.template.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.billykid.template.entity.Book;
import com.billykid.template.repository.BookRepository;
import com.billykid.template.utils.DTO.BookDTO;
import com.billykid.template.utils.parameters.BookParametersObject;
import com.billykid.template.utils.specification.BookSpecifications;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

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
    
}
