package com.billykid.template.entity;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
@Table(name="book")
public class Book {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @ManyToMany(mappedBy="bookList")
    private Set<Reservation> reservationList;

    @Column(name="title")
    private String title;

    @Column(name="description")
    private String description;

    @Column(name="book_cover")
    private String bookCoverUrl;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name="book_genres", joinColumns = @JoinColumn(name="book_id"))
    @Column(name="genre", nullable = false)
    private List<String> genres;
    
    @ManyToOne
    @JoinColumn(name="author",referencedColumnName="id")
    private Author author;

    @CreatedDate
    @Column(name="added_date")
    private Instant addedDate;

    @Column(name="volume_number")
    private Integer volume;
}
