package com.billykid.template.utils.mappers.book;

import org.springframework.stereotype.Component;

import com.billykid.template.entity.Book;
import com.billykid.template.utils.DTO.book.BookSummaryDTO;

@Component
public class BookSummaryMapper {

    public BookSummaryDTO toDTO(Book book) {
        return new BookSummaryDTO(book);
    }
}
