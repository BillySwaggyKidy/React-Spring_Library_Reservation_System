package com.billykid.template.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.billykid.template.repository.BookRepository;
import com.billykid.template.repository.ReservationRepository;
import com.billykid.template.repository.UserRepository;
import com.billykid.template.utils.DTO.BookDTO;
import com.billykid.template.utils.DTO.ReservationDTO;
import com.billykid.template.utils.enums.UserRole;
import com.billykid.template.entity.Author;
import com.billykid.template.entity.Book;
import com.billykid.template.entity.DBUser;
import com.billykid.template.entity.Reservation;
import com.billykid.template.utils.mappers.ReservationMapper;
import com.billykid.template.utils.parameters.ReservationParametersObject;


@Profile("test")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void tryFindingReservationByUsername() throws Exception {
        List<Reservation> reservations = List.of(
            Reservation.builder().id(1).user(new DBUser(1, "john.doe", "john@example.com", "pass", UserRole.ROLE_CUSTOMER, Instant.now())).beginDate(LocalDate.of(2000,1,1).atStartOfDay(ZoneId.systemDefault()).toInstant()).endDate(LocalDate.of(2000,1,11)).build(),
            Reservation.builder().id(2).user(new DBUser(1, "john.doe", "john@example.com", "pass", UserRole.ROLE_CUSTOMER, Instant.now())).beginDate(LocalDate.of(2001,1,1).atStartOfDay(ZoneId.systemDefault()).toInstant()).endDate(LocalDate.of(2001,1,11)).build(),
            Reservation.builder().id(3).user(new DBUser(1, "john.doe", "john@example.com", "pass", UserRole.ROLE_CUSTOMER, Instant.now())).beginDate(LocalDate.of(2002,1,1).atStartOfDay(ZoneId.systemDefault()).toInstant()).endDate(LocalDate.of(2002,1,11)).build()
        );

        when(reservationRepository.findByUser_UsernameContainingIgnoreCase(any(String.class))).thenReturn(reservations);

        List<ReservationDTO> result = reservationService.findReservationsByUserName("john", null);

        assertEquals(3, result.size());
        assertEquals(1, result.get(0).getUserID());
        assertEquals(1, result.get(1).getUserID());
        assertEquals(1, result.get(2).getUserID());
    }

    @Test
    void tryFindingReservationByBeginDate() throws Exception {
        List<Reservation> reservations = List.of(
            Reservation.builder().id(1).user(new DBUser(1, "john.doe", "john@example.com", "pass", UserRole.ROLE_CUSTOMER, Instant.now())).beginDate(LocalDate.of(2000,1,1).atStartOfDay(ZoneId.systemDefault()).toInstant()).endDate(LocalDate.of(2000,1,11)).build(),
            Reservation.builder().id(2).user(new DBUser(2, "Mary.babe", "mary@example.com", "pass", UserRole.ROLE_CUSTOMER, Instant.now())).beginDate(LocalDate.of(2000,1,1).atStartOfDay(ZoneId.systemDefault()).toInstant()).endDate(LocalDate.of(2000,1,11)).build(),
            Reservation.builder().id(3).user(new DBUser(3, "Larry.spook", "larry@example.com", "pass", UserRole.ROLE_CUSTOMER, Instant.now())).beginDate(LocalDate.of(2000,1,1).atStartOfDay(ZoneId.systemDefault()).toInstant()).endDate(LocalDate.of(2000,1,11)).build()
        );

        when(reservationRepository.findByBeginDateAfter(any(Instant.class))).thenReturn(reservations);

        List<ReservationDTO> result = reservationService.findReservationsByBeginDate(LocalDate.of(2000,1,1), null);

        assertEquals(3, result.size());
        assertEquals(LocalDate.of(2000,1,1), result.get(0).getBeginDate());
        assertEquals(LocalDate.of(2000,1,1), result.get(1).getBeginDate());
        assertEquals(LocalDate.of(2000,1,1), result.get(2).getBeginDate());
    }

    @Test
    void tryFindingReservationByEndDate() throws Exception {
        List<Reservation> reservations = List.of(
            Reservation.builder().id(1).user(new DBUser(1, "john.doe", "john@example.com", "pass", UserRole.ROLE_CUSTOMER, Instant.now())).beginDate(LocalDate.of(2000,1,1).atStartOfDay(ZoneId.systemDefault()).toInstant()).endDate(LocalDate.of(2000,1,11)).build(),
            Reservation.builder().id(2).user(new DBUser(2, "Mary.babe", "mary@example.com", "pass", UserRole.ROLE_CUSTOMER, Instant.now())).beginDate(LocalDate.of(2000,1,1).atStartOfDay(ZoneId.systemDefault()).toInstant()).endDate(LocalDate.of(2000,1,11)).build(),
            Reservation.builder().id(3).user(new DBUser(3, "Larry.spook", "larry@example.com", "pass", UserRole.ROLE_CUSTOMER, Instant.now())).beginDate(LocalDate.of(2000,1,1).atStartOfDay(ZoneId.systemDefault()).toInstant()).endDate(LocalDate.of(2000,1,11)).build()
        );

        when(reservationRepository.findByEndDateLessThanEqual(any(LocalDate.class))).thenReturn(reservations);

        List<ReservationDTO> result = reservationService.findReservationsByEndDate(LocalDate.of(2000,1,11), null);

        assertEquals(3, result.size());
        assertEquals(LocalDate.of(2000,1,11), result.get(0).getEndDate());
        assertEquals(LocalDate.of(2000,1,11), result.get(1).getEndDate());
        assertEquals(LocalDate.of(2000,1,11), result.get(2).getEndDate());
    }

    @Test
    void  tryFindingReservationByQueryParams() throws Exception {
        List<Reservation> reservations = List.of(
            Reservation.builder().id(1).user(new DBUser(1, "john.doe", "john@example.com", "pass", UserRole.ROLE_CUSTOMER, Instant.now())).beginDate(LocalDate.of(2000,1,1).atStartOfDay(ZoneId.systemDefault()).toInstant()).endDate(LocalDate.of(2000,1,11)).build(),
            Reservation.builder().id(2).user(new DBUser(2, "jane.joke", "mary@example.com", "pass", UserRole.ROLE_CUSTOMER, Instant.now())).beginDate(LocalDate.of(2000,1,1).atStartOfDay(ZoneId.systemDefault()).toInstant()).endDate(LocalDate.of(2000,1,11)).build(),
            Reservation.builder().id(3).user(new DBUser(3, "joe.spook", "larry@example.com", "pass", UserRole.ROLE_CUSTOMER, Instant.now())).beginDate(LocalDate.of(2000,1,1).atStartOfDay(ZoneId.systemDefault()).toInstant()).endDate(LocalDate.of(2000,1,11)).build()
        );

        when(reservationRepository.findAll(any(Specification.class),nullable(Pageable.class))).thenReturn(new PageImpl<>(reservations));

        List<ReservationDTO> result = reservationService.findReservationsByQueryParams(new ReservationParametersObject("j",LocalDate.of(2000,1,1),LocalDate.of(2000,1,11)), null);

        assertEquals(3, result.size());
        assertEquals(1, result.get(0).getUserID());
        assertEquals(2, result.get(1).getUserID());
        assertEquals(3, result.get(2).getUserID());
        assertEquals(LocalDate.of(2000,1,1), result.get(0).getBeginDate());
        assertEquals(LocalDate.of(2000,1,1), result.get(1).getBeginDate());
        assertEquals(LocalDate.of(2000,1,1), result.get(2).getBeginDate());
        assertEquals(LocalDate.of(2000,1,11), result.get(0).getEndDate());
        assertEquals(LocalDate.of(2000,1,11), result.get(1).getEndDate());
        assertEquals(LocalDate.of(2000,1,11), result.get(2).getEndDate());
    }

    @Test
    void tryGettingReservationContent() throws Exception {
        Integer reservationId = 1;
        Set<Book> books = Set.of(
            Book.builder().id(1).title("Captain underpants").description("This is a story about a funny hero").bookCoverUrl("https://picsum.photos/id/237/250").genres(List.of("Adventure", "Comedy")).volume(1).author(new Author(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 ))).build(),
            Book.builder().id(2).title("Captain underpants: Dr Kratus unchained").description("This is a story about a funny hero").bookCoverUrl("https://picsum.photos/id/237/240").genres(List.of("Adventure", "Comedy")).volume(2).author(new Author(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 ))).build(),
            Book.builder().id(3).title("Captain underpants: Finally peace").description("This is a story about a funny hero").bookCoverUrl("https://picsum.photos/id/237/230").genres(List.of("Adventure", "Comedy")).volume(3).author(new Author(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 ))).build()
        );
        Reservation reservation = Reservation.builder().id(1).user(new DBUser(1, "john.doe", "john@example.com", "pass", UserRole.ROLE_CUSTOMER, Instant.now())).beginDate(LocalDate.of(2000,1,1).atStartOfDay(ZoneId.systemDefault()).toInstant()).endDate(LocalDate.of(2000,1,11)).bookList(books).build();

        when(reservationRepository.findById(eq(1))).thenReturn(Optional.of(reservation));

        List<BookDTO> result = reservationService.findReservationContent(reservationId, null);

        List<String> expectedTitles = List.of(
        "Captain underpants",
        "Captain underpants: Dr Kratus unchained",
        "Captain underpants: Finally peace"
        );

        List<String> actualTitles = result.stream().map(BookDTO::getTitle).toList();

        assertEquals(3, result.size());
        assertTrue(actualTitles.containsAll(expectedTitles));
    }

    @Test
    void tryAddingNewReservation() throws Exception {
        Integer id = 1;
        DBUser user = new DBUser(1,"Billy","billy@gmail.com","3rg43s6eg36e7g",UserRole.ROLE_CUSTOMER, LocalDate.of(2000,01,11).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Set<Book> setBooks = Set.of(
            Book.builder().id(1).title("Captain underpants").description("This is a story about a funny hero").bookCoverUrl("https://picsum.photos/id/237/250").genres(List.of("Adventure", "Comedy")).volume(1).author(new Author(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 ))).build(),
            Book.builder().id(1).title("Captain underpants: Dr Kratus unchained").description("This is a story about a funny hero").bookCoverUrl("https://picsum.photos/id/237/240").genres(List.of("Adventure", "Comedy")).volume(2).author(new Author(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 ))).build(),
            Book.builder().id(1).title("Captain underpants: Finally peace").description("This is a story about a funny hero").bookCoverUrl("https://picsum.photos/id/237/230").genres(List.of("Adventure", "Comedy")).volume(3).author(new Author(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 ))).build()
        );
        List<Book> listBooks = List.of(
            Book.builder().id(1).title("Captain underpants").description("This is a story about a funny hero").bookCoverUrl("https://picsum.photos/id/237/250").genres(List.of("Adventure", "Comedy")).volume(1).author(new Author(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 ))).build(),
            Book.builder().id(1).title("Captain underpants: Dr Kratus unchained").description("This is a story about a funny hero").bookCoverUrl("https://picsum.photos/id/237/240").genres(List.of("Adventure", "Comedy")).volume(2).author(new Author(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 ))).build(),
            Book.builder().id(1).title("Captain underpants: Finally peace").description("This is a story about a funny hero").bookCoverUrl("https://picsum.photos/id/237/230").genres(List.of("Adventure", "Comedy")).volume(3).author(new Author(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 ))).build()
        );
        ReservationDTO sendReservation = new ReservationDTO(1,1,LocalDate.of(2000,1,1),LocalDate.of(2000,1,11),List.of(1,2,3));
        Reservation reservation = Reservation.builder().id(1).user(new DBUser(1, "Billy", "billy@gmail.com", "3rg43s6eg36e7g", UserRole.ROLE_CUSTOMER, Instant.now())).beginDate(LocalDate.of(2000,1,1).atStartOfDay(ZoneId.systemDefault()).toInstant()).endDate(LocalDate.of(2000,1,11)).bookList(setBooks).build();
        ReservationDTO newReservation = new ReservationDTO(1,1,LocalDate.of(2000,1,1),LocalDate.of(2000,1,11),List.of(1,2,3));

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(bookRepository.findAllById(anyList())).thenReturn(listBooks);
        when(reservationMapper.toEntity(any(ReservationDTO.class), any(DBUser.class), anyList())).thenReturn(reservation);
        when(reservationMapper.toDTO(any(Reservation.class))).thenReturn(newReservation);

        ReservationDTO result = reservationService.addNewReservation(sendReservation);

        assertEquals(1, result.getUserID());
        assertEquals(LocalDate.of(2000,1,1), result.getBeginDate());
        assertEquals(LocalDate.of(2000,1,11), result.getEndDate());
    }

    @Test
    void tryUpdatingReservation() throws Exception {
        Integer id = 1;
        DBUser user = new DBUser(2,"Larry","larry@gmail.com","3rg43s6eg36e7g",UserRole.ROLE_CUSTOMER, LocalDate.of(2000,01,11).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Set<Book> setBooks = Set.of(
            Book.builder().id(1).title("Captain underpants").description("This is a story about a funny hero").bookCoverUrl("https://picsum.photos/id/237/250").genres(List.of("Adventure", "Comedy")).volume(1).author(new Author(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 ))).build(),
            Book.builder().id(1).title("Captain underpants: Dr Kratus unchained").description("This is a story about a funny hero").bookCoverUrl("https://picsum.photos/id/237/240").genres(List.of("Adventure", "Comedy")).volume(2).author(new Author(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 ))).build(),
            Book.builder().id(1).title("Captain underpants: Finally peace").description("This is a story about a funny hero").bookCoverUrl("https://picsum.photos/id/237/230").genres(List.of("Adventure", "Comedy")).volume(3).author(new Author(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 ))).build()
        );
        List<Book> listBooks = List.of(
            Book.builder().id(1).title("Captain underpants").description("This is a story about a funny hero").bookCoverUrl("https://picsum.photos/id/237/250").genres(List.of("Adventure", "Comedy")).volume(1).author(new Author(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 ))).build(),
            Book.builder().id(1).title("Captain underpants: Dr Kratus unchained").description("This is a story about a funny hero").bookCoverUrl("https://picsum.photos/id/237/240").genres(List.of("Adventure", "Comedy")).volume(2).author(new Author(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 ))).build(),
            Book.builder().id(1).title("Captain underpants: Finally peace").description("This is a story about a funny hero").bookCoverUrl("https://picsum.photos/id/237/230").genres(List.of("Adventure", "Comedy")).volume(3).author(new Author(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 ))).build()
        );
        ReservationDTO newDTO = new ReservationDTO(null,2,null,null,null);
        Reservation oldReservation = Reservation.builder().id(1).user(new DBUser(1, "Billy", "billy@gmail.com", "3rg43s6eg36e7g", UserRole.ROLE_CUSTOMER, Instant.now())).beginDate(LocalDate.of(2000,1,1).atStartOfDay(ZoneId.systemDefault()).toInstant()).endDate(LocalDate.of(2000,1,11)).bookList(setBooks).build();
        ReservationDTO updatedDTO = new ReservationDTO(1,2,LocalDate.of(2000,1,1),LocalDate.of(2000,1,11),List.of(1,2,3));

        when(reservationRepository.findById(id)).thenReturn(Optional.of(oldReservation));
        when(userRepository.findById(eq(2))).thenReturn(Optional.of(user));
        when(bookRepository.findAllById(anyList())).thenReturn(listBooks);
        when(reservationMapper.toDTO(any(Reservation.class))).thenReturn(updatedDTO);

        ReservationDTO result = reservationService.updateReservation(id,newDTO);

        assertEquals(2, result.getUserID());

    }

    @Test
    void tryRemovingReservation() throws Exception {
        Integer id = 1;
        Reservation reservation = new Reservation();
        reservation.setId(id);
        ReservationDTO expectedDTO = new ReservationDTO();
        expectedDTO.setId(id);

        when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation));
        when(reservationMapper.toDTO(reservation)).thenReturn(expectedDTO);

        // When
        ReservationDTO result = reservationService.removeReservation(id);

        // Then
        verify(reservationRepository).findById(id);
        verify(reservationRepository).delete(reservation);
        verify(reservationMapper).toDTO(reservation);
        assertEquals(expectedDTO, result);
    }


}
