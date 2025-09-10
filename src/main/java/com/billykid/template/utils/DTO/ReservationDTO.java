package com.billykid.template.utils.DTO;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.billykid.template.entity.Reservation;
import com.billykid.template.utils.DTO.book.BookSummaryDTO;
import com.billykid.template.entity.Book;
import com.billykid.template.entity.DBUser;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReservationDTO {
    private Integer id;
    private Integer userID;
    private String username;
    private LocalDate beginDate;
    private LocalDate endDate;
    private List<Integer> bookIds;
    private List<BookSummaryDTO> content;

    public ReservationDTO(Integer id, Integer userID, String username, LocalDate beginDate, LocalDate endDate, List<Integer> bookIds) {
        this.id = id;
        this.userID = userID;
        this.username = username;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.bookIds = bookIds;
    }

    // Constructor to map entity to DTO
    public ReservationDTO(Reservation reservation) {
        this.id = reservation.getId();
        this.beginDate = reservation.getBeginDate().atZone(ZoneId.systemDefault()).toLocalDate();
        this.endDate = reservation.getEndDate();
        this.bookIds = null;
        
        // Avoid lazy loading issues
        DBUser user = reservation.getUser();
        if (user != null) {
            this.userID = user.getId();
            this.username = user.getUsername();
        } else {
            this.userID = null;
            this.username = null;
        }   
    }

    // Constructor to map entity to DTO
    public ReservationDTO(Reservation reservation, Set<Book> books) {
        this.id = reservation.getId();
        this.beginDate = reservation.getBeginDate().atZone(ZoneId.systemDefault()).toLocalDate();
        this.endDate = reservation.getEndDate();
        this.bookIds = null;
        this.content = books.stream().map(book -> new BookSummaryDTO(book)).toList();
        
        // Avoid lazy loading issues
        DBUser user = reservation.getUser();
        if (user != null) {
            this.userID = user.getId();
            this.username = user.getUsername();
        } else {
            this.userID = null;
            this.username = null;
        }   
    }
}