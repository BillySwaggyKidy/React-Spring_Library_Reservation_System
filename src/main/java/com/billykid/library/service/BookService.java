package com.billykid.library.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.billykid.library.entity.Author;
import com.billykid.library.entity.Book;
import com.billykid.library.entity.BookStatus;
import com.billykid.library.entity.Reservation;
import com.billykid.library.exception.AuthorNotFoundException;
import com.billykid.library.exception.BookNotFoundException;
import com.billykid.library.repository.AuthorRepository;
import com.billykid.library.repository.BookRepository;
import com.billykid.library.repository.ReservationRepository;
import com.billykid.library.utils.DTO.PagedResponse;
import com.billykid.library.utils.DTO.book.BookDetailsDTO;
import com.billykid.library.utils.DTO.book.BookSummaryDTO;
import com.billykid.library.utils.enums.BookQuality;
import com.billykid.library.utils.mappers.book.BookDetailsMapper;
import com.billykid.library.utils.mappers.book.BookSummaryMapper;
import com.billykid.library.utils.parameters.BookParametersObject;
import com.billykid.library.utils.specifications.BookSpecifications;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // Lombok auto-generates the constructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final ReservationRepository reservationRepository;

    private final BookSummaryMapper bookSummaryMapper;
    private final BookDetailsMapper bookDetailsMapper;

    public List<BookSummaryDTO> findBooksByTitle(String title, Pageable pageable) {
        List<Book> bookList = bookRepository.findByTitleContainingIgnoreCase(title);
        return bookList.stream().map(book -> new BookSummaryDTO(book)).collect(Collectors.toList());
    }

    public List<BookSummaryDTO> findBooksByAuthor(String author, Pageable pageable) {
        List<Book> bookList = bookRepository.findByAuthor_NameContainingIgnoreCase(author);
        return bookList.stream().map(book -> new BookSummaryDTO(book)).collect(Collectors.toList());
    }

    public List<BookSummaryDTO> findBooksByGenres(List<String> genres, Pageable pageable) {
        List<Book> bookList = bookRepository.findAll(BookSpecifications.hasGenre(genres));
        return bookList.stream().map(book -> new BookSummaryDTO(book)).collect(Collectors.toList());
    }

    public List<BookSummaryDTO> findBooksByAvailable(boolean available, Pageable pageable) {
        List<Book> bookList = bookRepository.findByBookStatus_IsAvailable(!available);
        return bookList.stream().map(book -> new BookSummaryDTO(book)).collect(Collectors.toList());
    }

    public PagedResponse<BookSummaryDTO> findBooksByQueryParams(BookParametersObject params, Pageable pageable) {
        Specification<Book> spec = buildSpecification(params);
        Page<Book> bookPage = bookRepository.findAll(spec, pageable);
        List<BookSummaryDTO> bookList = bookPage.getContent().stream().map(book -> new BookSummaryDTO(book)).collect(Collectors.toList());
        return new PagedResponse<BookSummaryDTO>(
            bookList,
            bookPage.getNumber(),
            bookPage.getSize(),
            bookPage.getTotalElements(),
            bookPage.getTotalPages()
        );
    }

    public BookDetailsDTO findBookDetails(Integer id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("Book with ID " + id + " not found"));
        BookDetailsDTO bookDetails = bookDetailsMapper.toDTO(book);
        if (!bookDetails.getStatus().isAvailable()) {
            Optional<Reservation> reservation = reservationRepository.findFirstByBookList_IdOrderByEndDateDesc(bookDetails.getId());
            if (reservation.isPresent()) {
                bookDetails.getStatus().setBeAvailableAt(reservation.get().getEndDate());
            }
        }
        return bookDetails;
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
        if (params.getIsReserved() != null) {
            spec = spec.and(BookSpecifications.isReserved(params.getIsReserved()));
        }

        return spec;
    }

    public BookSummaryDTO addNewBook(BookDetailsDTO bookDTO) {
        Author author = authorRepository.findById(bookDTO.getAuthorId()).orElseThrow(() -> new AuthorNotFoundException("Author with ID " + bookDTO.getId() + " not found"));
        Book newBook = bookDetailsMapper.toEntity(bookDTO, author, null);
        bookRepository.save(newBook);
        BookStatus bookStatus = BookStatus.builder().book(newBook).isAvailable(true).quality(BookQuality.NEW).build();
        newBook.setBookStatus(bookStatus);
        bookRepository.save(newBook);
        return new BookSummaryDTO(newBook);
    }

    public BookSummaryDTO updateBook(Integer id, BookDetailsDTO bookDTO) {
        Book existingBook = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("Book with ID " + id + " not found"));
        Author author = bookDTO.getAuthorId() != null ? authorRepository.findById(bookDTO.getAuthorId()).orElseThrow(() -> new AuthorNotFoundException("Author with ID " + bookDTO.getId() + " not found")) : null;
        bookDetailsMapper.updateEntity(existingBook, bookDTO, author);
        bookRepository.save(existingBook);
        return bookSummaryMapper.toDTO(existingBook);
    }

    public BookSummaryDTO removeBook(Integer id) {
        Book deletedBook = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("Book with ID " + id + " not found"));
        if (deletedBook.getBookStatus() != null) {
            deletedBook.setBookStatus(null); // This will trigger orphan removal
        }    
        bookRepository.delete(deletedBook);
        return bookSummaryMapper.toDTO(deletedBook);
    }
    
}
