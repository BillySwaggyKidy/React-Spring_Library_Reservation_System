package com.billykid.library.entity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
@Table(name="reservation")
@EntityListeners(AuditingEntityListener.class) // Enables @CreatedDate
public class Reservation {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @ManyToMany
    @JoinTable(name="reservation_content", joinColumns=@JoinColumn(name="reservation_id"), inverseJoinColumns=@JoinColumn(name="book_id"))
    private Set<Book> bookList;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName="id", nullable=true)
    private DBUser user;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(name = "begin_date")
    private LocalDate beginDate;

    @Column(name="end_date")
    private LocalDate endDate;


}
