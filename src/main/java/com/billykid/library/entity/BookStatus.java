package com.billykid.library.entity;


import java.time.Instant;

import com.billykid.library.utils.enums.BookQuality;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name="book_status")
public class BookStatus {

    @Id  // Primary key is also a foreign key
    @Column(name = "book_id")
    private Integer bookId;

    @OneToOne(optional = false)
    @MapsId  // Ensures book_id is both PK and FK
    @JoinColumn(name = "book_id")
    @JsonIgnore
    private Book book;

    @Column(name = "is_available", nullable = false)
    private boolean isAvailable;

    @Enumerated(EnumType.STRING)
    @Column(name = "quality", nullable = false)
    private BookQuality quality; // e.g., NEW, GOOD, DAMAGED

    @Column(name = "updated_at")
    private Instant updatedAt;
}
