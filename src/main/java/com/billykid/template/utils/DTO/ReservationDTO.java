package com.billykid.template.utils.DTO;

import java.util.Date;
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
    private Date beginDate;
    private Date endDate;
    private List<Integer> bookIds;

    // Constructor to map entity to DTO
    public ReservationDTO(Reservation reservation) {
        this.id = reservation.getId();
        this.beginDate = Date.from(reservation.getBeginDate());
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