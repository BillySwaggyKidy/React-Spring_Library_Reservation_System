package com.billykid.library.utils.mappers;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Component;

import com.billykid.library.entity.Book;
import com.billykid.library.entity.DBUser;
import com.billykid.library.entity.Reservation;
import com.billykid.library.utils.DTO.ReservationDTO;

@Component
public class ReservationMapper {
    public ReservationDTO toDTO(Reservation reservation) {
        return new ReservationDTO(reservation);
    }

    public ReservationDTO toDetailsDTO(Reservation reservation) {
        return new ReservationDTO(reservation, reservation.getBookList());
    }

    public Reservation toEntity(ReservationDTO dto, DBUser user, List<Book> books) {
        return Reservation.builder()
            .user(user)
            .bookList(new HashSet<>(books))
            .beginDate(dto.getBeginDate())
            .endDate(dto.getEndDate())
            .build();
    }

    public void updateEntity(Reservation existingReservation, ReservationDTO dto, DBUser user, List<Book> books) {

        if (user != null) {
            existingReservation.setUser(user);
        }
        if (books != null && !books.isEmpty()) {
            existingReservation.setBookList(new HashSet<>(books));
        }
        if (dto.getBeginDate() != null) {
            existingReservation.setBeginDate(dto.getBeginDate());
        }
        if (dto.getEndDate() != null) {
            existingReservation.setEndDate(dto.getEndDate());
        }
    }
}
