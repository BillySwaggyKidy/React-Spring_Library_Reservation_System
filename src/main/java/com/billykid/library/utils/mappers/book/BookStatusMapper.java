package com.billykid.library.utils.mappers.book;

import com.billykid.library.entity.BookStatus;
import com.billykid.library.utils.DTO.book.BookStatusDTO;
import com.billykid.library.utils.enums.BookQuality;

public class BookStatusMapper {
    public BookStatusDTO toDTO(BookStatus bookStatus) {
        return new BookStatusDTO(bookStatus);
    }

    public BookStatus toEntity(BookStatusDTO dto) {
        return BookStatus.builder()
            .isAvailable(dto.isAvailable())
            .quality(BookQuality.valueOf(dto.getQuality()))
            .build();
    }

    public void updateEntity(BookStatus existingBookStatus, BookStatusDTO dto) {
        if (dto.isAvailable() != existingBookStatus.isAvailable()) {
            existingBookStatus.setAvailable(dto.isAvailable());
        }
        if (!dto.getQuality().equals(existingBookStatus.getQuality().toString())) {
            existingBookStatus.setQuality(BookQuality.valueOf(dto.getQuality()));
        }
    }
}
