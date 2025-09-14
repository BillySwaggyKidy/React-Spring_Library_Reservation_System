package com.billykid.library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.billykid.library.service.BookService;
import com.billykid.library.utils.DTO.PagedResponse;
import com.billykid.library.utils.DTO.book.BookDetailsDTO;
import com.billykid.library.utils.DTO.book.BookSummaryDTO;
import com.billykid.library.utils.parameters.BookParametersObject;

import jakarta.annotation.security.RolesAllowed;



@RestController
@RequestMapping(path="/api")
public class BookController {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    private final BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    // Mapping HTTP GET requests to the /hello endpoint to this method
    @GetMapping("/hello")
    public String hello() {
        // Returning the injected message value
        return "Hello, you are in " + activeProfile + " mode";
    }

    @GetMapping("/books/title/{title}")
    public ResponseEntity<List<BookSummaryDTO>> getBookByTitle(@PathVariable String title, Pageable pageable) {
        List<BookSummaryDTO> books = bookService.findBooksByTitle(title, pageable);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/books/author/{author}")
    public ResponseEntity<List<BookSummaryDTO>> getBooksByAuthor(@PathVariable String author, Pageable pageable) {
        List<BookSummaryDTO> books = bookService.findBooksByAuthor(author, pageable);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/books/genres/{genres}")
    public ResponseEntity<List<BookSummaryDTO>> getBooksByGenres(@PathVariable List<String> genres, Pageable pageable) {
        List<BookSummaryDTO> books = bookService.findBooksByGenres(genres, pageable);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/books/reserved/{isReserved}")
    public ResponseEntity<List<BookSummaryDTO>> getBooksByAvailable(@PathVariable boolean isReserved, Pageable pageable) {
        List<BookSummaryDTO> books = bookService.findBooksByAvailable(isReserved, pageable);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/books")
    public ResponseEntity<PagedResponse<BookSummaryDTO>> getBooks(@RequestParam(required=false) String title, @RequestParam(required=false) String author, @RequestParam(required=false) List<String> genres, @RequestParam(required=false) Boolean isReserved, @PageableDefault(size = 10) Pageable pageable) {
        BookParametersObject parametersData = BookParametersObject.builder().title(title).author(author).genres(genres).isReserved(isReserved).build();
        PagedResponse<BookSummaryDTO> books = bookService.findBooksByQueryParams(parametersData, pageable);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<BookDetailsDTO> getBookDetails(@PathVariable Integer id) {
        BookDetailsDTO bookDetails = bookService.findBookDetails(id);
        return ResponseEntity.ok(bookDetails);
    }

    @PostMapping("/books/add")
    @RolesAllowed({"EMPLOYEE", "ADMIN"})
    public ResponseEntity<BookSummaryDTO> getDetailsOfBook(@RequestBody BookDetailsDTO newBook) {
        BookSummaryDTO book = bookService.addNewBook(newBook);
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    @PutMapping("books/update/{id}")
    @RolesAllowed({"EMPLOYEE", "ADMIN"})
    public ResponseEntity<BookSummaryDTO> putMethodName(@PathVariable Integer id, @RequestBody BookDetailsDTO book) {
        BookSummaryDTO updatedBook = bookService.updateBook(id, book);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("books/remove/{id}")
    @RolesAllowed({"EMPLOYEE", "ADMIN"})
    public ResponseEntity<BookSummaryDTO> deleteMethodName(@PathVariable Integer id) {
        BookSummaryDTO removedBook = bookService.removeBook(id);
        return ResponseEntity.ok(removedBook);
    }
    


}
