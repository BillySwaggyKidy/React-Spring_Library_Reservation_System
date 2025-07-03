package com.billykid.template.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Profile;

import com.billykid.template.entity.DBUser;
import com.billykid.template.utils.enums.UserRole;

@Profile("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private DBUser user1;
    private DBUser user2;
    private DBUser user3;

    @BeforeEach
    void setUp() {
        user1 = new DBUser(null, "john.doe", "john@example.com", "pass", UserRole.ROLE_CUSTOMER, Instant.now());
        user2 = new DBUser(null, "jane.smith", "jane@example.com", "pass", UserRole.ROLE_ADMIN, Instant.now());
        user3 = new DBUser(null, "johnny.appleseed", "johnny@example.com", "pass", UserRole.ROLE_CUSTOMER, Instant.now());

        userRepository.saveAll(List.of(user1, user2, user3));
    }

    @Test
    void testFindByUsername() {
        Optional<DBUser> user = userRepository.findByUsername("john.doe");
        assertTrue(user.isPresent());
        assertEquals("john.doe", user.get().getUsername());
    }

    @Test
    void testFindByUsernameContainingIgnoreCase() {
        List<DBUser> users = userRepository.findByUsernameContainingIgnoreCase("john");
        assertEquals(2, users.size());
        // Vérifie que les usernames retournés contiennent bien "john" (ignorant casse)
        assertTrue(users.stream().allMatch(u -> u.getUsername().toLowerCase().contains("john")));
    }

    @Test
    void testFindByEmailContainingIgnoreCase() {
        List<DBUser> users = userRepository.findByEmailContainingIgnoreCase("example.com");
        assertEquals(3, users.size());
    }

    @Test
    void testFindByRole() {
        List<DBUser> customers = userRepository.findByRole(UserRole.ROLE_CUSTOMER);
        assertEquals(2, customers.size());
        List<DBUser> admins = userRepository.findByRole(UserRole.ROLE_ADMIN);
        assertEquals(1, admins.size());
    }

    @Test
    void testFindByEmail() {
        Optional<DBUser> user = userRepository.findByEmail("jane@example.com");
        assertTrue(user.isPresent());
        assertEquals("jane.smith", user.get().getUsername());
    }
}
