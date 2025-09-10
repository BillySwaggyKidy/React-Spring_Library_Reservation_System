package com.billykid.template.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.billykid.template.entity.DBUser;
import com.billykid.template.exception.DBUserUserAlreadyExist;
import com.billykid.template.repository.UserRepository;
import com.billykid.template.utils.DTO.DBUserDTO;
import com.billykid.template.utils.enums.UserRole;
import com.billykid.template.utils.mappers.UserMapper;
import com.billykid.template.utils.parameters.UserParametersObject;


@Profile("test")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CustomUserDetailsServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;


    @Test
    void tryFindingUserByUsername() throws Exception {
        List<DBUser> users = List.of(
            new DBUser(1, "Billy", "billy@gmail.com", "68t21t6th46s5th46r8ht4", UserRole.ROLE_CUSTOMER, LocalDate.of(2000,01,11).atStartOfDay(ZoneId.systemDefault()).toInstant(), true),
            new DBUser(2, "Bob", "bob@gmail.com", "54g36e84g368esr43g6s8e43g684e36", UserRole.ROLE_EMPLOYEE, LocalDate.of(2000,01,11).atStartOfDay(ZoneId.systemDefault()).toInstant(), true),
            new DBUser(3, "Brade", "brade@gmail.com", "3348s368g436r84h3r68t4h6rh84", UserRole.ROLE_ADMIN, LocalDate.of(2000,01,11).atStartOfDay(ZoneId.systemDefault()).toInstant(),true)
        );

        when(userRepository.findByUsernameContainingIgnoreCase(any(String.class))).thenReturn(users);

        List<DBUserDTO> result = customUserDetailsService.findByUsernameContainingIgnoreCase("B", null);

        assertEquals(3, result.size());
        assertEquals("Billy", result.get(0).getUsername());
        assertEquals("Bob", result.get(1).getUsername());
        assertEquals("Brade", result.get(2).getUsername());
    }

    @Test
    void tryFindingUserByEmail() throws Exception {
        List<DBUser> users = List.of(
            new DBUser(1, "Billy", "billy@gmail.com", "68t21t6th46s5th46r8ht4", UserRole.ROLE_CUSTOMER, LocalDate.of(2000,01,11).atStartOfDay(ZoneId.systemDefault()).toInstant(), true),
            new DBUser(2, "Bob", "bob@gmail.com", "54g36e84g368esr43g6s8e43g684e36", UserRole.ROLE_EMPLOYEE, LocalDate.of(2000,01,11).atStartOfDay(ZoneId.systemDefault()).toInstant(), true),
            new DBUser(3, "Brade", "brade@gmail.com", "3348s368g436r84h3r68t4h6rh84", UserRole.ROLE_ADMIN, LocalDate.of(2000,01,11).atStartOfDay(ZoneId.systemDefault()).toInstant(), true)
        );

        when(userRepository.findByEmailContainingIgnoreCase(any(String.class))).thenReturn(users);

        List<DBUserDTO> result = customUserDetailsService.findByEmailContainingIgnoreCase("b", null);

        assertEquals(3, result.size());
        assertEquals("billy@gmail.com", result.get(0).getEmail());
        assertEquals("bob@gmail.com", result.get(1).getEmail());
        assertEquals("brade@gmail.com", result.get(2).getEmail());
    }

    @Test
    void tryFindingUserByRole() throws Exception {
        List<DBUser> users = List.of(
            new DBUser(1, "Billy", "billy@gmail.com", "68t21t6th46s5th46r8ht4", UserRole.ROLE_CUSTOMER, LocalDate.of(2000,01,11).atStartOfDay(ZoneId.systemDefault()).toInstant(), true),
            new DBUser(5, "Laila", "laila@gmail.com", "46s38r4h38r43h84sr3t6h84s3rth84", UserRole.ROLE_CUSTOMER, LocalDate.of(2000,01,11).atStartOfDay(ZoneId.systemDefault()).toInstant(), true),
            new DBUser(8, "Mary", "mary@gmail.com", "3s84h36d84h368rd4h368r4", UserRole.ROLE_CUSTOMER, LocalDate.of(2000,01,11).atStartOfDay(ZoneId.systemDefault()).toInstant(), true)
        );

        when(userRepository.findByRole(eq(UserRole.ROLE_CUSTOMER))).thenReturn(users);

        List<DBUserDTO> result = customUserDetailsService.findByRole(UserRole.ROLE_CUSTOMER, null);

        assertEquals(3, result.size());
        assertEquals("ROLE_CUSTOMER", result.get(0).getRole());
        assertEquals("ROLE_CUSTOMER", result.get(1).getRole());
        assertEquals("ROLE_CUSTOMER", result.get(2).getRole());
    }

    @Test
    void tryFindingUserByQueryParams() throws Exception {
        List<DBUser> users = List.of(
            new DBUser(1, "Mason", "mason@gmail.com", "68t21t6th46s5th46r8ht4", UserRole.ROLE_EMPLOYEE, LocalDate.of(2000,01,11).atStartOfDay(ZoneId.systemDefault()).toInstant(), true),
            new DBUser(5, "Maly", "maly@gmail.com", "3s684h36rdth436d4h368d4h", UserRole.ROLE_EMPLOYEE, LocalDate.of(2000,01,11).atStartOfDay(ZoneId.systemDefault()).toInstant(), true),
            new DBUser(8, "Moise", "moise@gmail.com", "3s84h36d84h368rd4h368r4", UserRole.ROLE_EMPLOYEE, LocalDate.of(2000,01,11).atStartOfDay(ZoneId.systemDefault()).toInstant(), true)
        );

        when(userRepository.findAll(any(Specification.class), nullable(Pageable.class))).thenReturn(new PageImpl<>(users));

        List<DBUserDTO> result = customUserDetailsService.findUsersByQueryParams(new UserParametersObject("M","m",UserRole.ROLE_EMPLOYEE), null).getContent();

        assertEquals(3, result.size());
        assertEquals("Mason", result.get(0).getUsername());
        assertEquals("Maly", result.get(1).getUsername());
        assertEquals("Moise", result.get(2).getUsername());
        assertEquals("mason@gmail.com", result.get(0).getEmail());
        assertEquals("maly@gmail.com", result.get(1).getEmail());
        assertEquals("moise@gmail.com", result.get(2).getEmail());
        assertEquals("ROLE_EMPLOYEE", result.get(0).getRole());
        assertEquals("ROLE_EMPLOYEE", result.get(1).getRole());
        assertEquals("ROLE_EMPLOYEE", result.get(2).getRole());
    }

    @Test
    void tryAddingNewUser() throws Exception {
        DBUserDTO dto = new DBUserDTO(1, "Mason", "mason@gmail.com", "68t21t6th46s5th46r8ht4", "ROLE_EMPLOYEE");
        DBUser newUser = new DBUser(1, "Mason", "mason@gmail.com", "68t21t6th46s5th46r8ht4", UserRole.ROLE_EMPLOYEE, LocalDate.of(2000,01,11).atStartOfDay(ZoneId.systemDefault()).toInstant(), true);

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("encryptedPassword");
        when(userMapper.toEntity(dto)).thenReturn(newUser);
        when(userRepository.save(any(DBUser.class))).thenReturn(newUser);
        when(userMapper.toDTO(any(DBUser.class))).thenReturn(dto);

        DBUserDTO result = customUserDetailsService.registerUser(dto);
        assertEquals("Mason", result.getUsername());
        assertEquals("mason@gmail.com", result.getEmail());
        assertEquals("ROLE_EMPLOYEE", result.getRole());

    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyExists() {
        // Given
        DBUserDTO dto = new DBUserDTO(1, "Mason", "mason@gmail.com", "secret", "ROLE_EMPLOYEE");
        DBUser existingUser = new DBUser(); // Peut être vide, on ne s'en sert pas

        // Simule un utilisateur existant
        when(userRepository.findByEmail("mason@gmail.com")).thenReturn(Optional.of(existingUser));

        // When + Then
        assertThrows(DBUserUserAlreadyExist.class, () -> {
            customUserDetailsService.registerUser(dto);
        });

        // Vérifie qu'aucune autre action n'a été tentée (en option)
        verify(userMapper, never()).toEntity(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void tryUpdatingUser() throws Exception {
        Integer id = 1;
        DBUserDTO dto = new DBUserDTO(1, "Marc", "marc@gmail.com", null, null);
        DBUser oldUser = new DBUser(1, "Mason", "mason@gmail.com", "68t21t6th46s5th46r8ht4", UserRole.ROLE_EMPLOYEE, LocalDate.of(2000,01,11).atStartOfDay(ZoneId.systemDefault()).toInstant(), true);
        DBUser newUser = new DBUser(1, "Marc", "marc@gmail.com", "68t21ttyj5146s5th46r8ht4", UserRole.ROLE_EMPLOYEE, LocalDate.of(2000,01,11).atStartOfDay(ZoneId.systemDefault()).toInstant(), true);
        DBUserDTO newDto = new DBUserDTO(1, "Marc", "marc@gmail.com", "68t21ttyj5146s5th46r8ht4", "ROLE_EMPLOYEE");
        
        when(userRepository.findById(id)).thenReturn(Optional.of(oldUser));
        when(userRepository.save(any(DBUser.class))).thenReturn(newUser);
        when(userMapper.toDTO(any(DBUser.class))).thenReturn(newDto);

        DBUserDTO result = customUserDetailsService.updateUser(id, dto);
        assertEquals("Marc", result.getUsername());
        assertEquals("marc@gmail.com", result.getEmail());
    }

    @Test
    void trySoftDeletingUser() throws Exception {

        Integer id = 1;
        DBUser user = new DBUser(
                id,
                "Mason",
                "mason@gmail.com",
                "68t21t6th46s5th46r8ht4",
                UserRole.ROLE_EMPLOYEE,
                LocalDate.of(2000, 1, 11).atStartOfDay(ZoneId.systemDefault()).toInstant(),
                true
        );
        DBUserDTO expectedDTO = new DBUserDTO(
                id,
                "Mason",
                "mason@gmail.com",
                "68t21t6th46s5th46r8ht4",
                "ROLE_EMPLOYEE"
        );

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(expectedDTO);

        // When
        DBUserDTO result = customUserDetailsService.softDeleteUser(id);

        // Then
        verify(userRepository).findById(id);
        verify(userRepository).save(user);
        verify(userMapper).toDTO(user);

        assertFalse(user.isActive(), "User should be marked as inactive");
        assertEquals(expectedDTO, result);
    }

    @Test
    void tryRemovingUser() throws Exception {

        Integer id = 1;
        DBUser user = new DBUser();
        user.setId(id);
        DBUserDTO expectedDTO = new DBUserDTO();
        expectedDTO.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(expectedDTO);

        // When
        DBUserDTO result = customUserDetailsService.removeUser(id);

        // Then
        verify(userRepository).findById(id);
        verify(userRepository).delete(user);
        verify(userMapper).toDTO(user);
        assertEquals(expectedDTO, result);
    }
}
