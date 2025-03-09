package com.billykid.template.entity;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
@Table(name="book")
@EntityListeners(AuditingEntityListener.class) // Enables @CreatedDate
public class Book {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @ManyToMany(mappedBy="bookList")
    @JsonIgnore
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

    @OneToOne(mappedBy = "book", cascade = CascadeType.ALL, fetch=FetchType.LAZY, orphanRemoval=true, optional = false)
    private BookStatus bookStatus;

    @CreatedDate
    @Column(name="added_date")
    private Instant addedDate;

    @Column(name="volume_number")
    private Integer volume;
}
