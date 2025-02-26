package com.billykid.template.utils.DTO;

import java.util.List;

import com.billykid.template.entity.Book;
import com.billykid.template.entity.BookStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDTO {
    private Integer id;
    private String title;
    private String description;
    private String bookCoverUrl;
    private List<String> genres;
    private String authorName;
    private Integer volume;
    private boolean available; // From BookStatus

    // Constructor to map entity to DTO
    public BookDTO(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.description = book.getDescription();
        this.bookCoverUrl = book.getBookCoverUrl();
        this.genres = book.getGenres();
        this.authorName = book.getAuthor().getName();
        this.volume = book.getVolume();
        
        // Avoid lazy loading issues
        BookStatus status = book.getBookStatus();
        if (status != null) {
            this.available = status.isAvailable();
        } else {
            this.available = false;
        }
    }
}
