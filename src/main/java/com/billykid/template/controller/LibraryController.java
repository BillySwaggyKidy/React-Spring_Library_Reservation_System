package com.billykid.template.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.billykid.template.service.BookService;
import com.billykid.template.utils.DTO.BookDTO;
import com.billykid.template.utils.parameters.BookParametersObject;

@RestController
@RequestMapping(path="/api")
public class LibraryController {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    private final BookService bookService;

    public LibraryController(BookService bookService){
        this.bookService = bookService;
    }

    // Mapping HTTP GET requests to the /hello endpoint to this method
    @GetMapping("/hello")
    public String hello() {
        // Returning the injected message value
        return "Hello, you are in " + activeProfile + " mode";
    }

    @GetMapping("/books/title/{title}")
    public List<BookDTO> getBookByTitle(@PathVariable String title, Pageable pageable) {
        List<BookDTO> books = bookService.findBooksByTitle(title, pageable);
        return books;
    }

    @GetMapping("/books/author/{author}")
    public List<BookDTO> getBooksByAuthor(@PathVariable String author, Pageable pageable) {
        List<BookDTO> books = bookService.findBooksByAuthor(author, pageable);
        return books;
    }

    @GetMapping("/books/genres/{genres}")
    public List<BookDTO> getBooksByGenres(@PathVariable List<String> genres, Pageable pageable) {
        List<BookDTO> books = bookService.findBooksByGenres(genres, pageable);
        return books;
    }

    @GetMapping("/books/reserved/{isReserved}")
    public List<BookDTO> getBooksByAvailable(@PathVariable boolean isReserved, Pageable pageable) {
        List<BookDTO> books = bookService.findBooksByAvailable(isReserved, pageable);
        return books;
    }

    @GetMapping("/books")
    public List<BookDTO> getBooks(@RequestParam(required=false) String title, @RequestParam(required=false) String author, @RequestParam(required=false) List<String> genres, @RequestParam(required=false) boolean isReserved, Pageable pageable) {
        BookParametersObject parametersData = BookParametersObject.builder().title(title).author(author).genres(genres).isReserved(isReserved).build();
        List<BookDTO> books = bookService.findBooksByQueryParams(parametersData, pageable);
        return books;
    }


}
