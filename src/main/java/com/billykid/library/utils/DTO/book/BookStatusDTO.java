package com.billykid.library.utils.DTO.book;

import java.time.LocalDate;

import com.billykid.library.entity.BookStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor 
public class BookStatusDTO {
    private boolean available;
    private String quality;
    private LocalDate beAvailableAt;

    public BookStatusDTO(boolean available, String quality) {
        this.available = available;
        this.quality = quality;
        this.beAvailableAt = null;
    }

    public BookStatusDTO(boolean available, String quality, LocalDate beAvailableAt) {
        this.available = available;
        this.quality = quality;
        this.beAvailableAt = beAvailableAt;
    }

    public BookStatusDTO(BookStatus bookStatus) {
        this.available = bookStatus.isAvailable();
        this.quality = bookStatus.getQuality().toString();
        this.beAvailableAt = null;
    }

    public BookStatusDTO(BookStatus bookStatus, LocalDate beAvailableAt) {
        this.available = bookStatus.isAvailable();
        this.quality = bookStatus.getQuality().toString();
        this.beAvailableAt = beAvailableAt;
    }
}
