package com.billykid.template.utils.mappers;

import org.springframework.stereotype.Component;

import com.billykid.template.entity.Author;
import com.billykid.template.entity.Book;
import com.billykid.template.entity.BookStatus;
import com.billykid.template.utils.DTO.BookDTO;
import com.billykid.template.utils.DTO.BookStatusDTO;
import com.billykid.template.utils.enums.BookCondition;

@Component
public class BookMapper {


    public BookDTO toDTO(Book book) {
        return new BookDTO(book);
    }

    public Book toEntity(BookDTO dto, Author author, BookStatus bookStatus) {
        return Book.builder()
            .id(dto.getId())
            .title(dto.getTitle())
            .author(author)
            .description(dto.getDescription())
            .bookCoverUrl(dto.getBookCoverUrl())
            .genres(dto.getGenres())
            .bookStatus(bookStatus)
            .volume(dto.getVolume())
            .build();
    }

    public void updateEntity(Book existingBook, BookDTO dto, Author author) {
        if (author != null) {
            existingBook.setAuthor(author);
        }
        if (dto.getTitle() != null) {
            existingBook.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            existingBook.setDescription(dto.getDescription());
        }
        if (dto.getBookCoverUrl() != null) {
            existingBook.setBookCoverUrl(dto.getBookCoverUrl());
        }
        if (dto.getGenres() != null) {
            existingBook.setGenres(dto.getGenres());
        }
        if (dto.getVolume() != null) {
            existingBook.setVolume(dto.getVolume());
        }
        if (dto.getStatus() != null) {
            BookStatusDTO dtoStatus = dto.getStatus();
            BookStatus updatedBookStatus = existingBook.getBookStatus();
            BookStatusMapper bookStatusMapper = new BookStatusMapper();
            bookStatusMapper.updateEntity(updatedBookStatus, dtoStatus);
            existingBook.setBookStatus(updatedBookStatus);
        }
        
    }
}
