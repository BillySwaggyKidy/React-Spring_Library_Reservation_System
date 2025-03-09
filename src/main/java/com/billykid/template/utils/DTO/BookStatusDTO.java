package com.billykid.template.utils.DTO;

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

    public BookStatusDTO(boolean available, String condition) {
        this.available = available;
        this.condition = condition;
    }

    public BookStatusDTO(BookStatus bookStatus) {
        this.available = bookStatus.isAvailable();
        this.condition = bookStatus.getCondition().toString();
    }
}
