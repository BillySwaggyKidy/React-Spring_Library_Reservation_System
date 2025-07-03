package com.billykid.template.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.billykid.template.service.ReservationService;
import com.billykid.template.utils.DTO.BookDTO;
import com.billykid.template.utils.DTO.ReservationDTO;
import com.billykid.template.utils.parameters.ReservationParametersObject;

import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping(path="/api")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService){
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations/user/{userName}")
    @RolesAllowed({"EMPLOYEE", "ADMIN"})
    public ResponseEntity<List<ReservationDTO>> getReservationsByUserName(@PathVariable String userName, Pageable pageable) {
        List<ReservationDTO> reservationList = reservationService.findReservationsByUserName(userName, pageable);
        return ResponseEntity.ok(reservationList);
    }

    @GetMapping("/reservations/start/{beginDate}")
    @RolesAllowed({"EMPLOYEE", "ADMIN"})
    public ResponseEntity<List<ReservationDTO>> getReservationsByBeginDate(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate beginDate, Pageable pageable) {
        List<ReservationDTO> reservationList = reservationService.findReservationsByBeginDate(beginDate, pageable);
        return ResponseEntity.ok(reservationList); 
    }

    @GetMapping("/reservations/end/{endDate}")
    @RolesAllowed({"EMPLOYEE", "ADMIN"})
    public ResponseEntity<List<ReservationDTO>> getReservationsByEndDate(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate, Pageable pageable) {
        List<ReservationDTO> reservationList = reservationService.findReservationsByEndDate(endDate, pageable);
        return ResponseEntity.ok(reservationList);
    }

    @GetMapping("/reservations")
    @RolesAllowed({"EMPLOYEE", "ADMIN"})
    public ResponseEntity<List<ReservationDTO>> getReservations(@RequestParam(required=false) String username, @RequestParam(required=false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate beginDate, @RequestParam(required=false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate, Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ReservationParametersObject reservationsParameters = ReservationParametersObject.builder().userName(username).beginDate(beginDate).endDate(endDate).build();
        List<ReservationDTO> reservationList = reservationService.findReservationsByQueryParams(reservationsParameters, pageable);
        return ResponseEntity.ok(reservationList);
    }

    @GetMapping("/reservations/view/{id}")
    @RolesAllowed({"EMPLOYEE", "ADMIN"})
    public ResponseEntity<List<BookDTO>> getReservationContent(@PathVariable Integer id, Pageable pageable) {
        List<BookDTO> books = reservationService.findReservationContent(id, pageable);
        return ResponseEntity.ok(books);
    }

    @PostMapping("/reservations/add")
    @RolesAllowed({"CUSTOMER", "EMPLOYEE", "ADMIN"})
    public ResponseEntity<ReservationDTO> addReservation(@RequestBody ReservationDTO reservation) {
        ReservationDTO newReservation = reservationService.addNewReservation(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(newReservation);
    }

    @PutMapping("/reservations/update/{id}")
    @RolesAllowed({"EMPLOYEE", "ADMIN"})
    public ResponseEntity<ReservationDTO> updateReservation(@PathVariable Integer id, @RequestBody ReservationDTO reservation) {
        ReservationDTO updatedReservation = reservationService.updateReservation(id, reservation);
        return ResponseEntity.ok(updatedReservation);
    }

    @DeleteMapping("/reservations/remove/{id}")
    @RolesAllowed({"EMPLOYEE", "ADMIN"})
    public ResponseEntity<ReservationDTO> removeReservation(@PathVariable Integer id) {
        ReservationDTO removedReservation = reservationService.removeReservation(id);
        return ResponseEntity.ok(removedReservation);
    }


}
