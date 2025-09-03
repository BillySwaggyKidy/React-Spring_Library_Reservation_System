package com.billykid.template.utils.DTO.book;

import java.time.LocalDate;
import java.util.List;

import com.billykid.template.entity.Book;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookDetailsDTO extends BookSummaryDTO {
    private String description;
    private List<String> genres;
    private Integer authorId;
    private LocalDate publishDate;
    private Integer volume;
    private Integer totalPages;


    public BookDetailsDTO(Integer id, String title, String description, String bookCoverUrl, List<String> genres, BookStatusDTO status, Integer authorId, String authorName, LocalDate publishDate, Integer volume, Integer totalPages) {
        super(id, title, bookCoverUrl, authorName, status);
        this.description = description;
        this.genres = genres;
        this.authorId = authorId;
        this.volume = volume;
        this.publishDate = publishDate;
        this.totalPages = totalPages;
    }

    // Constructor to map entity to DTO
    public BookDetailsDTO(Book book) {
        super(book);
        this.description = book.getDescription();
        this.genres = book.getGenres();
        this.authorId = book.getAuthor().getId();
        this.volume = book.getVolume();
        this.publishDate = book.getPublishDate();
        this.totalPages = book.getTotalPages();
    }
}
