package com.billykid.template.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.billykid.template.config.SpringSecurityConfiguration;
import com.billykid.template.service.CustomUserDetailsService;
import com.billykid.template.utils.DTO.DBUserDTO;
import com.billykid.template.utils.enums.UserRole;
import com.billykid.template.utils.parameters.UserParametersObject;
import com.billykid.template.utils.properties.CorsProperties;

@Profile("test")
@WebMvcTest(controllers = UserController.class)
@EnableConfigurationProperties(CorsProperties.class)
@AutoConfigureMockMvc(addFilters = true)
@Import(SpringSecurityConfiguration.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    CustomUserDetailsService customUserDetailsService;

    @Test
    void shouldReturnUserByName() throws Exception {
        List<DBUserDTO> users = List.of(
            new DBUserDTO(1, "Billy", "billy@gmail.com", "68t21t6th46s5th46r8ht4", "CUSTOMER"),
            new DBUserDTO(2, "Bob", "bobbob@gmail.com", "54g36e84g368esr43g6s8e43g684e36", "EMPLOYEE"),
            new DBUserDTO(3, "Brade", "brade@gmail.com", "3348s368g436r84h3r68t4h6rh84", "ADMIN")
        );

        when(customUserDetailsService.findByUsernameContainingIgnoreCase(eq("B"), any(Pageable.class))).thenReturn(users);

        mockMvc.perform(get("/api/users/name/B?page=0&size=2&sort=name,asc"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(3))
        .andExpect(jsonPath("$[0].username").value("Billy"))
        .andExpect(jsonPath("$[1].username").value("Bob"))
        .andExpect(jsonPath("$[2].username").value("Brade"));
    }

    @Test
    void shouldReturnUserByEmail() throws Exception {
        List<DBUserDTO> users = List.of(
            new DBUserDTO(1, "Billy", "billy@gmail.com", "68t21t6th46s5th46r8ht4", "CUSTOMER"),
            new DBUserDTO(2, "Bob", "bobbob@gmail.com", "54g36e84g368esr43g6s8e43g684e36", "EMPLOYEE"),
            new DBUserDTO(3, "Brade", "brade@gmail.com", "3348s368g436r84h3r68t4h6rh84", "ADMIN")
        );

        when(customUserDetailsService.findByEmailContainingIgnoreCase(eq("b"), any(Pageable.class))).thenReturn(users);

        mockMvc.perform(get("/api/users/email/b?page=0&size=2&sort=name,asc"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(3))
        .andExpect(jsonPath("$[0].email").value("billy@gmail.com"))
        .andExpect(jsonPath("$[1].email").value("bobbob@gmail.com"))
        .andExpect(jsonPath("$[2].email").value("brade@gmail.com"));
    }

    @Test
    void shouldReturnUserByRole() throws Exception {
        List<DBUserDTO> users = List.of(
            new DBUserDTO(1, "Billy", "billy@gmail.com", "68t21t6th46s5th46r8ht4", "CUSTOMER"),
            new DBUserDTO(5, "Laila", "laila@gmail.com", "46s38r4h38r43h84sr3t6h84s3rth84", "CUSTOMER"),
            new DBUserDTO(8, "Mary", "mary@gmail.com", "3s84h36d84h368rd4h368r4", "CUSTOMER")
        );

        when(customUserDetailsService.findByRole(eq(UserRole.ROLE_CUSTOMER), any(Pageable.class))).thenReturn(users);

        mockMvc.perform(get("/api/users/role/ROLE_CUSTOMER?page=0&size=2&sort=name,asc"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(3))
        .andExpect(jsonPath("$[0].username").value("Billy"))
        .andExpect(jsonPath("$[1].username").value("Laila"))
        .andExpect(jsonPath("$[2].username").value("Mary"));
    }

    @Test
    void shouldReturnUserByFilters() throws Exception {
        List<DBUserDTO> users = List.of(
            new DBUserDTO(1, "Mason", "mason@gmail.com", "68t21t6th46s5th46r8ht4", "EMPLOYEE"),
            new DBUserDTO(5, "Maly", "baily@gmail.com", "3s684h36rdth436d4h368d4h", "EMPLOYEE"),
            new DBUserDTO(8, "Moise", "moise@gmail.com", "3s84h36d84h368rd4h368r4", "EMPLOYEE")
        );

        when(customUserDetailsService.findUsersByQueryParams(any(UserParametersObject.class), any(Pageable.class))).thenReturn(users);

        mockMvc.perform(get("/api/users?username=M&email=b&role=ROLE_EMPLOYEE&page=0&size=2&sort=name,asc"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(3))
        .andExpect(jsonPath("$[0].username").value("Mason"))
        .andExpect(jsonPath("$[1].username").value("Maly"))
        .andExpect(jsonPath("$[2].username").value("Moise"));
    }

    @Test
    void tryAddingNewUser() throws Exception {
        DBUserDTO user = new DBUserDTO(1, "Mason", "mason@gmail.com", "68t21t6th46s5th46r8ht4", "EMPLOYEE");

        when(customUserDetailsService.registerUser(any(DBUserDTO.class))).thenReturn(user);

        mockMvc.perform(post("/api/users/add")
        .contentType("application/json")
        .content("""
            {
                "username": "Mason",
                "email": "mason@gmail.com",
                "password": "68t21t6th46s5th46r8ht4",
                "role": "EMPLOYEE"
            }
            """))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.username").value("Mason"))
        .andExpect(jsonPath("$.email").value("mason@gmail.com"))
        .andExpect(jsonPath("$.role").value("EMPLOYEE"));
    }

    @Test
    void tryUpdatingUser() throws Exception {
        DBUserDTO user = new DBUserDTO(1, "Mason", "mason123@gmail.com", "68t21t6th46s5th46r8ht4", "EMPLOYEE");

        when(customUserDetailsService.updateUser(anyInt(),any(DBUserDTO.class))).thenReturn(user);

        mockMvc.perform(post("/api/users/update/1")
        .contentType("application/json")
        .content("""
            {
                "email": "mason123@gmail.com",
            }
            """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value("mason123@gmail.com"));
    }

    @Test
    void tryDeletingUser() throws Exception {
        DBUserDTO user = new DBUserDTO(1, "Mason", "mason123@gmail.com", "68t21t6th46s5th46r8ht4", "EMPLOYEE");

        when(customUserDetailsService.removeUser(anyInt())).thenReturn(user);

        mockMvc.perform(post("/api/users/remove/1")).andExpect(status().isOk());
    }

}
