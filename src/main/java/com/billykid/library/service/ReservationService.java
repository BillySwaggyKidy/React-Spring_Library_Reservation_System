package com.billykid.library.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.billykid.library.entity.Book;
import com.billykid.library.entity.DBUser;
import com.billykid.library.entity.Reservation;
import com.billykid.library.exception.BookNotAvailableForReservation;
import com.billykid.library.exception.DBUserNotFoundException;
import com.billykid.library.exception.ReservationNotFoundException;
import com.billykid.library.repository.BookRepository;
import com.billykid.library.repository.ReservationRepository;
import com.billykid.library.repository.UserRepository;
import com.billykid.library.utils.DTO.PagedResponse;
import com.billykid.library.utils.DTO.ReservationDTO;
import com.billykid.library.utils.mappers.ReservationMapper;
import com.billykid.library.utils.parameters.ReservationParametersObject;
import com.billykid.library.utils.specifications.ReservationSpecifications;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // Lombok auto-generates the constructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    private final ReservationMapper reservationMapper;

    public List<ReservationDTO> findReservationsByUserName(String userName, Pageable pageable) {
        List<Reservation> reservationList = reservationRepository.findByUser_UsernameContainingIgnoreCase(userName);
        return reservationList.stream().map(ReservationDTO::new).collect(Collectors.toList());
    }

    public List<ReservationDTO> findReservationsByBeginDate(LocalDate beginDate, Pageable pageable) {
        List<Reservation> reservationList = reservationRepository.findByBeginDateAfter(beginDate);
        return reservationList.stream().map(ReservationDTO::new).collect(Collectors.toList());
    }

    public List<ReservationDTO> findReservationsByEndDate(LocalDate endDate, Pageable pageable) {
        List<Reservation> reservationList = reservationRepository.findByEndDateLessThanEqual(endDate);
        return reservationList.stream().map(ReservationDTO::new).collect(Collectors.toList());
    }

    public PagedResponse<ReservationDTO> findReservationsByQueryParams(ReservationParametersObject params, Pageable pageable) {
        Specification<Reservation> spec = buildSpecification(params);
        Page<Reservation> reservationPage = reservationRepository.findAll(spec, pageable);
        List<ReservationDTO> reservationList = reservationPage.getContent().stream().map(ReservationDTO::new).collect(Collectors.toList());
        return new PagedResponse<ReservationDTO>(
            reservationList,
            reservationPage.getNumber(),
            reservationPage.getSize(),
            reservationPage.getTotalElements(),
            reservationPage.getTotalPages()
        );
    }

    private Specification<Reservation> buildSpecification(ReservationParametersObject params) {
        Specification<Reservation> spec = Specification.where(null);

        if (params.getUserName() != null) {
            spec = spec.and(ReservationSpecifications.hasUsername(params.getUserName()));
        }
        if (params.getBeginDate() != null) {
            spec = spec.and(ReservationSpecifications.hasBeginDate(params.getBeginDate()));
        }
        if (params.getEndDate() != null) {
            spec = spec.and(ReservationSpecifications.hasEndDate(params.getEndDate()));
        }

        return spec;
    }

    public ReservationDTO findReservationDetails(Integer id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new ReservationNotFoundException("Reservation with ID: " + id + " not found"));
        return reservationMapper.toDetailsDTO(reservation);
    }

    public ReservationDTO addNewReservation(ReservationDTO reservationDTO) {
        DBUser user = userRepository.findById(reservationDTO.getUserID()).orElseThrow(() -> new DBUserNotFoundException("User with ID: " + reservationDTO.getUserID() + " not found"));
        List<Book> bookList = bookRepository.findAllById(reservationDTO.getBookIds());
        List<Integer> bookIdNotAvailable = bookList.stream().filter((book)->!book.getBookStatus().isAvailable()).map((book)->book.getId()).toList();
        if (bookIdNotAvailable.size() == 0) {
            reservationRepository.updateBookAvailable(false, reservationDTO.getBookIds());
        }
        else {
            String listOfBooksIDNotAvailable = bookIdNotAvailable.stream().map(String::valueOf).collect(Collectors.joining(","));
            throw new BookNotAvailableForReservation("Some books are not available for reservation: [" + listOfBooksIDNotAvailable + "]");
        }
        Reservation reservation = reservationMapper.toEntity(reservationDTO, user, bookList);
        
        reservationRepository.save(reservation);
        return reservationMapper.toDTO(reservation);
    }

    public ReservationDTO updateReservation(Integer id, ReservationDTO reservationDTO) {
        Reservation existingReservation = reservationRepository.findById(id).orElseThrow(() -> new ReservationNotFoundException("Reservation with ID: " + id + " not found"));
        DBUser user = userRepository.findById(reservationDTO.getUserID()).orElseThrow(() -> new DBUserNotFoundException("User with ID: " + reservationDTO.getUserID() + " not found"));
        reservationMapper.updateEntity(existingReservation, reservationDTO, user, null);
        reservationRepository.save(existingReservation);
        return reservationMapper.toDTO(existingReservation);
    }

    public ReservationDTO retrieveReservationContent(Integer id, ReservationDTO reservationDTO) {
        Reservation existingReservation = reservationRepository.findById(id).orElseThrow(() -> new ReservationNotFoundException("Reservation with ID: " + id + " not found"));
        DBUser user = userRepository.findById(reservationDTO.getUserID()).orElseThrow(() -> new DBUserNotFoundException("User with ID: " + reservationDTO.getUserID() + " not found"));
        List<Integer> bookIdList = bookRepository.findAllById(reservationDTO.getBookIds()).stream().map((book)->book.getId()).toList();
        reservationRepository.updateBookAvailable(true, bookIdList);
        existingReservation.setEndDate(reservationDTO.getEndDate());
        reservationRepository.save(existingReservation);
        return reservationMapper.toDTO(existingReservation);
    }

    public ReservationDTO removeReservation(Integer id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new ReservationNotFoundException("Reservation with ID: " + id + " not found"));
        List<Integer> bookIdList = reservation.getBookList().stream().map((book)->book.getId()).toList();
        reservationRepository.updateBookAvailable(true, bookIdList);
        reservationRepository.delete(reservation);
        return reservationMapper.toDTO(reservation);
    } 
    
}
