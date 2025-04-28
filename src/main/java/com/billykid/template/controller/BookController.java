package com.billykid.template.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
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

import com.billykid.template.service.BookService;
import com.billykid.template.utils.DTO.BookDTO;
import com.billykid.template.utils.parameters.BookParametersObject;

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
    public ResponseEntity<List<BookDTO>> getBookByTitle(@PathVariable String title, Pageable pageable) {
        List<BookDTO> books = bookService.findBooksByTitle(title, pageable);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/books/author/{author}")
    public ResponseEntity<List<BookDTO>> getBooksByAuthor(@PathVariable String author, Pageable pageable) {
        List<BookDTO> books = bookService.findBooksByAuthor(author, pageable);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/books/genres/{genres}")
    public ResponseEntity<List<BookDTO>> getBooksByGenres(@PathVariable List<String> genres, Pageable pageable) {
        List<BookDTO> books = bookService.findBooksByGenres(genres, pageable);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/books/reserved/{isReserved}")
    public ResponseEntity<List<BookDTO>> getBooksByAvailable(@PathVariable boolean isReserved, Pageable pageable) {
        List<BookDTO> books = bookService.findBooksByAvailable(isReserved, pageable);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/books")
    public ResponseEntity<List<BookDTO>> getBooks(@RequestParam(required=false) String title, @RequestParam(required=false) String author, @RequestParam(required=false) List<String> genres, @RequestParam(required=false) boolean isReserved, Pageable pageable) {
        BookParametersObject parametersData = BookParametersObject.builder().title(title).author(author).genres(genres).isReserved(isReserved).build();
        List<BookDTO> books = bookService.findBooksByQueryParams(parametersData, pageable);
        return ResponseEntity.ok(books);
    }

    @PostMapping("/books/add")
    @RolesAllowed({"EMPLOYEE", "ADMIN"})
    public ResponseEntity<BookDTO> addBook(@RequestBody BookDTO newBook) {
        BookDTO book = bookService.addNewBook(newBook);
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    @PutMapping("books/update/{id}")
    @RolesAllowed({"EMPLOYEE", "ADMIN"})
    public ResponseEntity<BookDTO> putMethodName(@PathVariable Integer id, @RequestBody BookDTO book) {
        BookDTO updatedBook = bookService.updateBook(id, book);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("books/remove/{id}")
    @RolesAllowed({"EMPLOYEE", "ADMIN"})
    public ResponseEntity<BookDTO> deleteMethodName(@PathVariable Integer id) {
        BookDTO removedBook = bookService.removeBook(id);
        return ResponseEntity.ok(removedBook);
    }
    


}
