package com.billykid.library.utils.mappers.book;

import org.springframework.stereotype.Component;

import com.billykid.library.entity.Book;
import com.billykid.library.utils.DTO.book.BookSummaryDTO;

@Component
public class BookSummaryMapper {

    public BookSummaryDTO toDTO(Book book) {
        return new BookSummaryDTO(book);
    }
}
