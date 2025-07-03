package com.billykid.template.utils.DTO;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import com.billykid.template.entity.Reservation;
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
    private LocalDate beginDate;
    private LocalDate endDate;
    private List<Integer> bookIds;

    public ReservationDTO(Integer id, Integer userID, LocalDate beginDate, LocalDate endDate, List<Integer> bookIds) {
        this.id = id;
        this.userID = userID;
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
        } else {
            this.userID = null;
        }   
    }
}