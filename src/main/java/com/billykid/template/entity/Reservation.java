package com.billykid.template.entity;

import java.util.Date;
import java.time.Instant;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @JoinColumn(name="user_id", referencedColumnName="id", nullable=false)
    private User user;

    @CreatedDate
    @Column(name = "begin_date", columnDefinition = "TIMESTAMP")
    private Instant beginDate;

    @Column(name="end_date")
    private Date endDate;


}
