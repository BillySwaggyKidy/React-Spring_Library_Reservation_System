package com.billykid.template.utils.mappers.book;

import org.springframework.stereotype.Component;

import com.billykid.template.entity.Author;
import com.billykid.template.entity.Book;
import com.billykid.template.entity.BookStatus;
import com.billykid.template.utils.DTO.book.BookDetailsDTO;
import com.billykid.template.utils.DTO.book.BookStatusDTO;

@Component
public class BookDetailsMapper {
    public BookDetailsDTO toDTO(Book book) {
        return new BookDetailsDTO(book);
    }

    public Book toEntity(BookDetailsDTO dto, Author author, BookStatus bookStatus) {
        return Book.builder()
            .id(dto.getId())
            .title(dto.getTitle())
            .bookCoverUrl(dto.getBookCoverUrl())
            .genres(dto.getGenres())
            .volume(dto.getVolume())
            .author(author)
            .publishDate(dto.getPublishDate())
            .totalPages(dto.getTotalPages())
            .bookStatus(bookStatus)
            .build();
    }

    public void updateEntity(Book existingBook, BookDetailsDTO dto, Author author) {
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
        if (dto.getPublishDate() != null) {
            existingBook.setPublishDate(dto.getPublishDate());
        }
        if (dto.getTotalPages() != null) {
            existingBook.setTotalPages(dto.getTotalPages());
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
