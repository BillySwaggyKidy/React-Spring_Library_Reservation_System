package com.billykid.template.utils.mappers.book;

import com.billykid.template.entity.BookStatus;
import com.billykid.template.utils.DTO.book.BookStatusDTO;
import com.billykid.template.utils.enums.BookCondition;

public class BookStatusMapper {
    public BookStatusDTO toDTO(BookStatus bookStatus) {
        return new BookStatusDTO(bookStatus);
    }

    public BookStatus toEntity(BookStatusDTO dto) {
        return BookStatus.builder()
            .isAvailable(dto.isAvailable())
            .condition(BookCondition.valueOf(dto.getCondition()))
            .build();
    }

    public void updateEntity(BookStatus existingBookStatus, BookStatusDTO dto) {
        if (dto.isAvailable() != existingBookStatus.isAvailable()) {
            existingBookStatus.setAvailable(dto.isAvailable());
        }
        if (!dto.getCondition().equals(existingBookStatus.getCondition().toString())) {
            existingBookStatus.setCondition(BookCondition.valueOf(dto.getCondition()));
        }
    }
}
