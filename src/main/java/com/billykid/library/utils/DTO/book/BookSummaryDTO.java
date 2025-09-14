package com.billykid.library.utils.DTO.book;

import com.billykid.library.entity.Book;
import com.billykid.library.entity.BookStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookSummaryDTO {
    private Integer id;
    private String title;
    private String bookCoverUrl;
    private String authorName;
    private BookStatusDTO status;

    public BookSummaryDTO(Integer id, String title, String bookCoverUrl, String authorName, BookStatusDTO status) {
        this.id = id;
        this.title = title;
        this.bookCoverUrl = bookCoverUrl;
        this.authorName = authorName;
        this.status = status;
    }

    // Constructor to map entity to DTO
    public BookSummaryDTO(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.bookCoverUrl = book.getBookCoverUrl();
        this.authorName = book.getAuthor().getName();
        
        // Avoid lazy loading issues
        BookStatus bookStatus = book.getBookStatus();
        if (bookStatus != null) {
            this.status = new BookStatusDTO(bookStatus.isAvailable(),bookStatus.getQuality().toString());
        }
    }
}
