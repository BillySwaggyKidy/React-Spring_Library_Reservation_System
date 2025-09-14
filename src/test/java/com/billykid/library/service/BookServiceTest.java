package com.billykid.library.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.billykid.library.entity.Author;
import com.billykid.library.entity.Book;
import com.billykid.library.entity.BookStatus;
import com.billykid.library.entity.DBUser;
import com.billykid.library.entity.Reservation;
import com.billykid.library.repository.AuthorRepository;
import com.billykid.library.repository.BookRepository;
import com.billykid.library.repository.ReservationRepository;
import com.billykid.library.service.BookService;
import com.billykid.library.utils.DTO.book.BookDetailsDTO;
import com.billykid.library.utils.DTO.book.BookStatusDTO;
import com.billykid.library.utils.DTO.book.BookSummaryDTO;
import com.billykid.library.utils.enums.BookQuality;
import com.billykid.library.utils.enums.UserRole;
import com.billykid.library.utils.mappers.book.BookDetailsMapper;
import com.billykid.library.utils.mappers.book.BookSummaryMapper;
import com.billykid.library.utils.parameters.BookParametersObject;

@Profile("test")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private BookSummaryMapper bookSummaryMapper;

    @Mock
    private BookDetailsMapper bookDetailsMapper;

    @InjectMocks
    private BookService bookService;

    @Test
    void tryFindingBookByTitle() throws Exception {
        // Given
        List<Book> books = List.of(
            Book.builder().id(1).title("Captain underpants").description("This is a story about a funny hero").bookCoverUrl("https://picsum.photos/id/237/250").genres(List.of("Adventure", "Comedy")).volume(1).author(new Author(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 ))).build(),
            Book.builder().id(2).title("Captain underpants: Dr Kratus unchained").description("This is a story about a funny hero").bookCoverUrl("https://picsum.photos/id/237/240").genres(List.of("Adventure", "Comedy")).volume(2).author(new Author(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 ))).build(),
            Book.builder().id(3).title("Captain underpants: Finally peace").description("This is a story about a funny hero").bookCoverUrl("https://picsum.photos/id/237/230").genres(List.of("Adventure", "Comedy")).volume(3).author(new Author(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 ))).build()
        );

        when(bookRepository.findByTitleContainingIgnoreCase(eq("Captain"))).thenReturn(books);

        List<BookSummaryDTO> result = bookService.findBooksByTitle("Captain", null);

        assertEquals(3, result.size());
        assertEquals("Captain underpants", result.get(0).getTitle());
        assertEquals("Captain underpants: Dr Kratus unchained", result.get(1).getTitle());
        assertEquals("Captain underpants: Finally peace", result.get(2).getTitle());
    }

    @Test
    void tryFindingBookByAuthor() throws Exception {
        // Given
        List<Book> books = List.of(
            Book.builder().id(1).title("Captain underpants").description("This is a story about a funny hero").bookCoverUrl("https://picsum.photos/id/237/250").genres(List.of("Adventure", "Comedy")).volume(1).author(new Author(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 ))).build(),
            Book.builder().id(2).title("Captain underpants: Dr Kratus unchained").description("This is a story about a funny hero").bookCoverUrl("https://picsum.photos/id/237/240").genres(List.of("Adventure", "Comedy")).volume(2).author(new Author(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 ))).build(),
            Book.builder().id(3).title("Captain underpants: Finally peace").description("This is a story about a funny hero").bookCoverUrl("https://picsum.photos/id/237/230").genres(List.of("Adventure", "Comedy")).volume(3).author(new Author(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 ))).build()
        );

        when(bookRepository.findByAuthor_NameContainingIgnoreCase(eq("John Parry"))).thenReturn(books);

        List<BookSummaryDTO> result = bookService.findBooksByAuthor("John Parry", null);

        assertEquals(3, result.size());
        assertEquals("Captain underpants", result.get(0).getTitle());
        assertEquals("Captain underpants: Dr Kratus unchained", result.get(1).getTitle());
        assertEquals("Captain underpants: Finally peace", result.get(2).getTitle());
    }

    @Test
    void tryFindingBookByGenres() throws Exception {
        // Given
        List<Book> books = List.of(
            Book.builder().id(1).title("Captain underpants").description("This is a story about a funny hero").bookCoverUrl("https://picsum.photos/id/237/250").genres(List.of("Adventure", "Comedy")).volume(1).author(new Author(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 ))).build(),
            Book.builder().id(2).title("Captain underpants: Dr Kratus unchained").description("This is a story about a funny hero").bookCoverUrl("https://picsum.photos/id/237/240").genres(List.of("Adventure", "Comedy")).volume(2).author(new Author(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 ))).build(),
            Book.builder().id(3).title("Captain underpants: Finally peace").description("This is a story about a funny hero").bookCoverUrl("https://picsum.photos/id/237/230").genres(List.of("Adventure", "Comedy")).volume(3).author(new Author(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 ))).build()
        );

        when(bookRepository.findAll(ArgumentMatchers.<Specification<Book>>any())).thenReturn(books);

        List<BookSummaryDTO> result = bookService.findBooksByGenres(List.of("Adventure", "Comedy"), null);

        assertEquals(3, result.size());
        assertEquals("Captain underpants", result.get(0).getTitle());
        assertEquals("Captain underpants: Dr Kratus unchained", result.get(1).getTitle());
        assertEquals("Captain underpants: Finally peace", result.get(2).getTitle());
    }

    @Test
    void tryFindingBookByAvailability() throws Exception {
        // Given
        List<Book> books = List.of(
            Book.builder().id(1).title("Captain underpants").description("This is a story about a funny hero").bookCoverUrl("https://picsum.photos/id/237/250").genres(List.of("Adventure", "Comedy")).volume(1).author(new Author(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 ))).bookStatus(BookStatus.builder().bookId(1).quality(BookQuality.NEW).isAvailable(true).build()).build(),
            Book.builder().id(2).title("Captain underpants: Dr Kratus unchained").description("This is a story about a funny hero").bookCoverUrl("https://picsum.photos/id/237/240").genres(List.of("Adventure", "Comedy")).volume(2).author(new Author(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 ))).bookStatus(BookStatus.builder().bookId(2).quality(BookQuality.NEW).isAvailable(true).build()).build(),
            Book.builder().id(3).title("Captain underpants: Finally peace").description("This is a story about a funny hero").bookCoverUrl("https://picsum.photos/id/237/230").genres(List.of("Adventure", "Comedy")).volume(3).author(new Author(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 ))).bookStatus(BookStatus.builder().bookId(3).quality(BookQuality.NEW).isAvailable(true).build()).build()
        );

        when(bookRepository.findByBookStatus_IsAvailable(anyBoolean())).thenReturn(books);

        List<BookSummaryDTO> result = bookService.findBooksByAvailable(true, null);

        assertEquals(3, result.size());
        assertEquals(true, result.get(0).getStatus().isAvailable());
        assertEquals(true, result.get(1).getStatus().isAvailable());
        assertEquals(true, result.get(2).getStatus().isAvailable());
    }

    @Test
    void tryFindingBookByFilters() throws Exception {
        // Given
        List<Book> books = List.of(
            Book.builder().id(1).title("Captain underpants").description("This is a story about a funny hero").bookCoverUrl("https://picsum.photos/id/237/250").genres(List.of("Adventure", "Comedy")).volume(1).author(new Author(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 ))).bookStatus(BookStatus.builder().bookId(1).quality(BookQuality.NEW).isAvailable(true).build()).build(),
            Book.builder().id(2).title("Captain underpants: Dr Kratus unchained").description("This is a story about a funny hero").bookCoverUrl("https://picsum.photos/id/237/240").genres(List.of("Adventure", "Comedy")).volume(2).author(new Author(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 ))).bookStatus(BookStatus.builder().bookId(2).quality(BookQuality.NEW).isAvailable(true).build()).build(),
            Book.builder().id(3).title("Captain underpants: Finally peace").description("This is a story about a funny hero").bookCoverUrl("https://picsum.photos/id/237/230").genres(List.of("Adventure", "Comedy")).volume(3).author(new Author(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 ))).bookStatus(BookStatus.builder().bookId(3).quality(BookQuality.NEW).isAvailable(true).build()).build()
        );

        when(bookRepository.findAll(ArgumentMatchers.<Specification<Book>>any(), nullable(Pageable.class))).thenReturn(new PageImpl<>(books));

        List<BookSummaryDTO> result = bookService.findBooksByQueryParams(new BookParametersObject("Captain underpants", "John Parry", List.of("Adventure", "Comedy"), true),null).getContent();

        assertEquals(3, result.size());
        assertEquals("Captain underpants", result.get(0).getTitle());
        assertEquals("Captain underpants: Dr Kratus unchained", result.get(1).getTitle());
        assertEquals(true, result.get(0).getStatus().isAvailable());
        assertEquals(true, result.get(1).getStatus().isAvailable());
        assertEquals(true, result.get(2).getStatus().isAvailable());
    }

    @Test
    void tryFindingBookDetails() throws Exception {
        Book bookDetails = Book.builder().id(1).title("Captain underpants").description("This is a story about a funny hero").bookCoverUrl("https://picsum.photos/id/237/250").genres(List.of("Adventure", "Comedy")).volume(1).author(new Author(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 ))).bookStatus(BookStatus.builder().bookId(1).quality(BookQuality.NEW).isAvailable(true).build()).build();
        BookDetailsDTO bookDetailsDTO = new BookDetailsDTO(1, "Captain underpants", "This is a story about a funny hero", "https://picsum.photos/id/237/250", List.of("Adventure","Comedy"), new BookStatusDTO(true, "NEW"), 1, "John Doe", LocalDate.ofYearDay(2013, 5), 1, 199);
        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(bookDetails));
        when(bookDetailsMapper.toDTO(any(Book.class))).thenReturn(bookDetailsDTO);
        when(reservationRepository.findFirstByBookList_IdOrderByEndDateDesc(anyInt())).thenReturn(Optional.of(Reservation.builder().id(1).user(new DBUser(1, "john.doe", "john@example.com", "pass", UserRole.ROLE_CUSTOMER, Instant.now(), true)).beginDate(LocalDate.of(2000,1,1)).endDate(LocalDate.of(2000,1,11)).build()));

        BookDetailsDTO result = bookService.findBookDetails(1);

        assertEquals("Captain underpants", result.getTitle());
        assertEquals(true, result.getStatus().isAvailable());
    }

    @Test
    void tryAddingNewBook() throws Exception {
        Integer id = 1;
        Author existingAuthor = new Author(1, "Old Name", "Old bio", LocalDate.of(1980, 1, 1));
        Book book = Book.builder().id(1).title("Captain underpants").description("This is a story about a funny hero").bookCoverUrl("https://picsum.photos/id/237/250").genres(List.of("Adventure", "Comedy")).volume(1).author(new Author(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 ))).bookStatus(BookStatus.builder().bookId(1).quality(BookQuality.NEW).isAvailable(true).build()).build();
        BookDetailsDTO bookDTO = new BookDetailsDTO(1, "Captain underpants", "This is a story about a funny hero", "https://picsum.photos/id/237/250", List.of("Adventure","Comedy"), new BookStatusDTO(true, "NEW"), 1, "John Doe", LocalDate.ofYearDay(2013, 5), 1, 199);
        
        when(authorRepository.findById(id)).thenReturn(Optional.of(existingAuthor));
        when(bookDetailsMapper.toEntity(bookDTO, existingAuthor, null)).thenReturn(book);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookSummaryDTO result = bookService.addNewBook(bookDTO);
        assertEquals("Captain underpants", result.getTitle());
        assertEquals(true, result.getStatus().isAvailable());

    }

    @Test
    void tryUpdatingExistingBook() throws Exception {
        Integer id = 1;
        Book existingBook = Book.builder().id(1).title("Captain underpants").description("This is a story about a funny hero").bookCoverUrl("https://picsum.photos/id/237/250").genres(List.of("Adventure", "Comedy")).volume(1).author(new Author(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 ))).bookStatus(BookStatus.builder().bookId(1).quality(BookQuality.NEW).isAvailable(true).build()).build();
        Author existingAuthor = new Author(1, "Old Name", "Old bio", LocalDate.of(1980, 1, 1));
        Book newBook = Book.builder().id(1).title("Captain underpants (New Edition)").description("This is a story about a wacky hero").bookCoverUrl("https://picsum.photos/id/237/250").genres(List.of("Adventure", "Comedy")).volume(1).author(new Author(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 ))).bookStatus(BookStatus.builder().bookId(1).quality(BookQuality.NEW).isAvailable(true).build()).build();
        BookDetailsDTO bookDTO = new BookDetailsDTO(1, "Captain underpants (New Edition)", "This is a story about a wacky hero", "https://picsum.photos/id/237/250", List.of("Adventure","Comedy"), new BookStatusDTO(true, "NEW"), 1,"John Doe", LocalDate.ofYearDay(2017, 6), 1, 115);
        
        when(bookRepository.findById(id)).thenReturn(Optional.of(existingBook));
        when(authorRepository.findById(id)).thenReturn(Optional.of(existingAuthor));
        when(bookRepository.save(any(Book.class))).thenReturn(newBook);
        when(bookSummaryMapper.toDTO(any(Book.class))).thenReturn(bookDTO);

        BookSummaryDTO result = bookService.updateBook(1, bookDTO);
        assertEquals("Captain underpants (New Edition)", result.getTitle());

    }

    @Test
    void tryRemovingBook() throws Exception {

        // Given
        Integer bookId = 1;
        Book book = new Book();
        book.setId(bookId);
        BookDetailsDTO expectedDTO = new BookDetailsDTO();
        expectedDTO.setId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookSummaryMapper.toDTO(book)).thenReturn(expectedDTO);

        // When
        BookSummaryDTO result = bookService.removeBook(bookId);

        // Then
        verify(bookRepository).findById(bookId);
        verify(bookRepository).delete(book);
        verify(bookSummaryMapper).toDTO(book);
        assertEquals(expectedDTO, result);

    }
        
}
