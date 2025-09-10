package com.billykid.template.repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.billykid.template.entity.Reservation;

import jakarta.transaction.Transactional;

public interface ReservationRepository extends JpaRepository<Reservation, Integer>, JpaSpecificationExecutor<Reservation> {
    public List<Reservation> findByUser_UsernameContainingIgnoreCase(String userName);
    public List<Reservation> findByBeginDateAfter(Instant beginDate);
    public List<Reservation> findByEndDateLessThanEqual(LocalDate endDate);
    public Optional<Reservation> findFirstByBookList_IdOrderByEndDateDesc(Integer bookId);

    @Modifying
    @Transactional
    @Query("UPDATE BookStatus bs SET bs.isAvailable = :available WHERE bs.bookId IN :ids")
    public int updateBookAvailable(@Param("available") Boolean available, @Param("ids") List<Integer> ids);
}
