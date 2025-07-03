package com.billykid.template.utils.DTO;

import java.util.List;

import com.billykid.template.entity.Book;
import com.billykid.template.entity.BookStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookDTO {
    private Integer id;
    private String title;
    private String description;
    private String bookCoverUrl;
    private List<String> genres;
    private BookStatusDTO status;
    private Integer authorId;
    private Integer volume;


    public BookDTO(Integer id, String title, String description, String bookCoverUrl, List<String> genres, BookStatusDTO status, Integer authorId, Integer volume) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.bookCoverUrl = bookCoverUrl;
        this.genres = genres;
        this.status = status;
        this.authorId = authorId;
        this.volume = volume;
    }

    // Constructor to map entity to DTO
    public BookDTO(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.description = book.getDescription();
        this.bookCoverUrl = book.getBookCoverUrl();
        this.genres = book.getGenres();
        this.authorId = book.getAuthor().getId();
        this.volume = book.getVolume();
        
        // Avoid lazy loading issues
        BookStatus bookStatus = book.getBookStatus();
        if (bookStatus != null) {
            this.status = new BookStatusDTO(bookStatus.isAvailable(),bookStatus.getCondition().toString());
        }
    }
}
