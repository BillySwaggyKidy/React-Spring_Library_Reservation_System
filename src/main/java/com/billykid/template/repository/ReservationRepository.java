package com.billykid.template.repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.billykid.template.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Integer>, JpaSpecificationExecutor<Reservation> {
    public List<Reservation> findByUser_UsernameContainingIgnoreCase(String userName);
    public List<Reservation> findByBeginDateAfter(Instant beginDate);
    public List<Reservation> findByEndDateLessThanEqual(LocalDate endDate);
}
