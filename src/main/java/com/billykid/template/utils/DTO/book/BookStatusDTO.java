package com.billykid.template.utils.DTO.book;

import java.time.LocalDate;

import org.springframework.cglib.core.Local;

import com.billykid.template.entity.BookStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor 
public class BookStatusDTO {
    private boolean available;
    private String condition;
    private LocalDate beAvailableAt;

    public BookStatusDTO(boolean available, String condition) {
        this.available = available;
        this.condition = condition;
        this.beAvailableAt = null;
    }

    public BookStatusDTO(boolean available, String condition, LocalDate beAvailableAt) {
        this.available = available;
        this.condition = condition;
        this.beAvailableAt = beAvailableAt;
    }

    public BookStatusDTO(BookStatus bookStatus) {
        this.available = bookStatus.isAvailable();
        this.condition = bookStatus.getCondition().toString();
        this.beAvailableAt = null;
    }

    public BookStatusDTO(BookStatus bookStatus, LocalDate beAvailableAt) {
        this.available = bookStatus.isAvailable();
        this.condition = bookStatus.getCondition().toString();
        this.beAvailableAt = beAvailableAt;
    }
}
