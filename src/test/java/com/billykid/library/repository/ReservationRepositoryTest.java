package com.billykid.library.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Profile;

import com.billykid.library.entity.DBUser;
import com.billykid.library.entity.Reservation;
import com.billykid.library.repository.ReservationRepository;
import com.billykid.library.repository.UserRepository;
import com.billykid.library.utils.enums.UserRole;

@Profile("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReservationRepositoryTest {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    private DBUser user1;

    private Reservation reservation1;
    private Reservation reservation2;

    @BeforeEach
    void setUp() {
        reservationRepository.deleteAll();
        userRepository.deleteAll();

        user1 = new DBUser(null, "john.doe", "john@example.com", "pass", UserRole.ROLE_CUSTOMER, Instant.now(), true);
        user1 = userRepository.save(user1);

        reservation1 = Reservation.builder()
            .user(user1)
            .beginDate(LocalDate.of(2025, 7, 1))
            .endDate(LocalDate.of(2025, 7, 10))
            .build();

        reservation2 = Reservation.builder()
            .user(user1)
            .beginDate(LocalDate.of(2025, 8, 1))
            .endDate(LocalDate.of(2025, 8, 10))
            .build();

        reservationRepository.saveAll(List.of(reservation1, reservation2));
    }

    @Test
    void testFindByUser_UsernameContainingIgnoreCase() {
        List<Reservation> results = reservationRepository.findByUser_UsernameContainingIgnoreCase("JOHN");
        assertEquals(2, results.size());
    }

    @Test
    void testFindByBeginDateAfter() {
        LocalDate date = LocalDate.of(2025,07,15);
        List<Reservation> results = reservationRepository.findByBeginDateAfter(date);

        assertEquals(1, results.size());
        assertEquals(reservation2.getId(), results.get(0).getId());
    }


    @Test
    void testFindByEndDateLessThanEqual() {
        LocalDate date = LocalDate.of(2025, 7, 15);
        List<Reservation> results = reservationRepository.findByEndDateLessThanEqual(date);
        assertEquals(1, results.size());
        assertEquals(reservation1.getId(), results.get(0).getId());
    }
}
