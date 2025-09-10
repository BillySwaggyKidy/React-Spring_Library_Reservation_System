package com.billykid.template.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.billykid.template.config.SpringSecurityConfiguration;
import com.billykid.template.service.CustomUserDetailsService;
import com.billykid.template.service.ReservationService;
import com.billykid.template.utils.DTO.PagedResponse;
import com.billykid.template.utils.DTO.ReservationDTO;
import com.billykid.template.utils.DTO.book.BookStatusDTO;
import com.billykid.template.utils.DTO.book.BookSummaryDTO;
import com.billykid.template.utils.parameters.ReservationParametersObject;
import com.billykid.template.utils.properties.CorsProperties;

@Profile("test")
@WebMvcTest(controllers = ReservationController.class)
@EnableConfigurationProperties(CorsProperties.class)
@AutoConfigureMockMvc(addFilters = true)
@Import(SpringSecurityConfiguration.class)
public class ReservationControllerTest {
    @Autowired
	private MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;


    @Test
    @WithMockUser(username = "John", roles = {"CUSTOMER"})
    void shouldReturnReservationByUserNameAsCustomerRole() throws Exception {
        List<ReservationDTO> reservations = List.of(
            new ReservationDTO(1,1,"John",LocalDate.of(2000,2,1),LocalDate.of(2000,2,11), List.of(1,2,3)),
            new ReservationDTO(2,1,"John",LocalDate.of(2001,2,1),LocalDate.of(2001,2,11), List.of(1,2,3)),
            new ReservationDTO(3,1,"John",LocalDate.of(2002,2,1),LocalDate.of(2002,2,11), List.of(1,2,3))
        );

        when(reservationService.findReservationsByUserName(eq("Billy"), any(Pageable.class))).thenReturn(reservations);

        mockMvc.perform(get("/api/reservations/user/Billy?page=0&size=2&sort=name,asc")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "John", roles = {"EMPLOYEE", "ADMIN"})
    void shouldReturnReservationByUserNameAsEmployeeOrAdminRole() throws Exception {
        List<ReservationDTO> reservations = List.of(
            new ReservationDTO(1,1,"John",LocalDate.of(2000,2,1),LocalDate.of(2000,2,11), List.of(1,2,3)),
            new ReservationDTO(2,1,"John",LocalDate.of(2001,2,1),LocalDate.of(2001,2,11), List.of(1,2,3)),
            new ReservationDTO(3,1,"John",LocalDate.of(2002,2,1),LocalDate.of(2002,2,11), List.of(1,2,3))
        );

        when(reservationService.findReservationsByUserName(eq("Billy"), any(Pageable.class))).thenReturn(reservations);

        mockMvc.perform(get("/api/reservations/user/Billy?page=0&size=2&sort=name,asc"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(3))
            .andExpect(jsonPath("$[0].beginDate").value("2000-02-01"))
            .andExpect(jsonPath("$[1].beginDate").value("2001-02-01"))
			.andExpect(jsonPath("$[2].beginDate").value("2002-02-01"));
    }

    @Test
    @WithMockUser(username = "John", roles = {"CUSTOMER"})
    void shouldReturnReservationByBeginDateAsCustomerRole() throws Exception {
        List<ReservationDTO> reservations = List.of(
            new ReservationDTO(1,1,"John",LocalDate.of(2000,2,1),LocalDate.of(2000,2,11), List.of(1,2,3)),
            new ReservationDTO(2,1,"John",LocalDate.of(2000,2,1),LocalDate.of(2000,2,11), List.of(2,3,4)),
            new ReservationDTO(3,1,"John",LocalDate.of(2000,2,1),LocalDate.of(2000,2,11), List.of(4,5,6))
        );

        when(reservationService.findReservationsByBeginDate(eq(LocalDate.of(2000,2,1)), any(Pageable.class))).thenReturn(reservations);

        mockMvc.perform(get("/api/reservations/start/2000-02-01?page=0&size=2&sort=name,asc")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "John", roles = {"EMPLOYEE", "ADMIN"})
    void shouldReturnReservationByBeginDateAsEmployeeOrAdminRole() throws Exception {
        List<ReservationDTO> reservations = List.of(
            new ReservationDTO(1,1,"John",LocalDate.of(2000,2,1),LocalDate.of(2000,2,11), List.of(1,2,3)),
            new ReservationDTO(2,1,"John",LocalDate.of(2000,2,1),LocalDate.of(2000,2,11), List.of(2,3,4)),
            new ReservationDTO(3,1,"John",LocalDate.of(2000,2,1),LocalDate.of(2000,2,11), List.of(4,5,6))
        );

        when(reservationService.findReservationsByBeginDate(eq(LocalDate.of(2000,2,1)), any(Pageable.class))).thenReturn(reservations);

        mockMvc.perform(get("/api/reservations/start/2000-02-01?page=0&size=2&sort=name,asc"))
        .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(3))
            .andExpect(jsonPath("$[0].beginDate").value("2000-02-01"))
            .andExpect(jsonPath("$[1].beginDate").value("2000-02-01"))
			.andExpect(jsonPath("$[2].beginDate").value("2000-02-01"));
    }

    @Test
    @WithMockUser(username = "John", roles = {"CUSTOMER"})
    void shouldReturnReservationByEndDateAsCustomerRole() throws Exception {
        List<ReservationDTO> reservations = List.of(
            new ReservationDTO(1,1,"John",LocalDate.of(2000,2,1),LocalDate.of(2000,2,11), List.of(1,2,3)),
            new ReservationDTO(2,1,"John",LocalDate.of(2000,2,1),LocalDate.of(2000,2,11), List.of(2,3,4)),
            new ReservationDTO(3,1,"John",LocalDate.of(2000,2,1),LocalDate.of(2000,2,11), List.of(4,5,6))
        );

        when(reservationService.findReservationsByEndDate(eq(LocalDate.of(2000,2,11)), any(Pageable.class))).thenReturn(reservations);

        mockMvc.perform(get("/api/reservations/end/2000-02-11?page=0&size=2&sort=name,asc")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "John", roles = {"EMPLOYEE", "ADMIN"})
    void shouldReturnReservationByEndDateAsEmployeeOrAdminRole() throws Exception {
        List<ReservationDTO> reservations = List.of(
            new ReservationDTO(1,1,"John",LocalDate.of(2000,2,1),LocalDate.of(2000,2,11), List.of(1,2,3)),
            new ReservationDTO(2,1,"John",LocalDate.of(2000,2,1),LocalDate.of(2000,2,11), List.of(2,3,4)),
            new ReservationDTO(3,1,"John",LocalDate.of(2000,2,1),LocalDate.of(2000,2,11), List.of(4,5,6))
        );

        when(reservationService.findReservationsByEndDate(eq(LocalDate.of(2000,2,11)), any(Pageable.class))).thenReturn(reservations);

        mockMvc.perform(get("/api/reservations/end/2000-02-11?page=0&size=2&sort=name,asc"))
        .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(3))
            .andExpect(jsonPath("$[0].endDate").value("2000-02-11"))
            .andExpect(jsonPath("$[1].endDate").value("2000-02-11"))
			.andExpect(jsonPath("$[2].endDate").value("2000-02-11"));
    }

    @Test
    @WithMockUser(username = "John", roles = {"CUSTOMER"})
    void shouldReturnReservationByFiltersAsCustomer() throws Exception {
        List<ReservationDTO> reservations = List.of(
            new ReservationDTO(1,1,"John",LocalDate.of(2000,2,1),LocalDate.of(2000,2,11), List.of(1,2,3)),
            new ReservationDTO(2,1,"John",LocalDate.of(2000,2,1),LocalDate.of(2000,2,11), List.of(2,3,4)),
            new ReservationDTO(3,1,"John",LocalDate.of(2000,2,1),LocalDate.of(2000,2,11), List.of(4,5,6))
        );
        PagedResponse<ReservationDTO> pagedResponse = new PagedResponse<>(
            reservations,
            0,
            2,
            3,
            2
        );

        when(reservationService.findReservationsByQueryParams(any(ReservationParametersObject.class), any(Pageable.class))).thenReturn(pagedResponse);

        mockMvc.perform(get("/api/reservations?username=Billy&beginDate=2000-02-01&endDate=2000-02-11&page=0&size=2&sort=name,asc")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "John", roles = {"EMPLOYEE", "ADMIN"})
    void shouldReturnReservationByFiltersAsEmployeeOrAdminRole() throws Exception {
        List<ReservationDTO> reservations = List.of(
            new ReservationDTO(1,1,"John",LocalDate.of(2000,2,1),LocalDate.of(2000,2,11), List.of(1,2,3)),
            new ReservationDTO(2,1,"John",LocalDate.of(2000,2,1),LocalDate.of(2000,2,11), List.of(2,3,4)),
            new ReservationDTO(3,1,"John",LocalDate.of(2000,2,1),LocalDate.of(2000,2,11), List.of(4,5,6))
        );
        PagedResponse<ReservationDTO> pagedResponse = new PagedResponse<>(
            reservations,
            0,
            2,
            3,
            2
        );

        when(reservationService.findReservationsByQueryParams(any(ReservationParametersObject.class), any(Pageable.class))).thenReturn(pagedResponse);

        mockMvc.perform(get("/api/reservations?username=Billy&beginDate=2000-02-01&endDate=2000-02-11&page=0&size=2&sort=name,asc"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(3))
        .andExpect(jsonPath("$.content[0].endDate").value("2000-02-11"))
        .andExpect(jsonPath("$.content[1].endDate").value("2000-02-11"))
        .andExpect(jsonPath("$.content[2].endDate").value("2000-02-11"));
    }

    @Test
    @WithMockUser(username="John", roles={"CUSTOMER"})
    void tryGettingReservationDetailsAsCustomerRole() throws Exception {
        List<BookSummaryDTO> reservationDetails = List.of(
            new BookSummaryDTO(1, "Captain underpants", "https://picsum.photos/id/237/250", "John Doe", new BookStatusDTO(true, "NEW")),
			new BookSummaryDTO(2, "Captain underpants: Dr Kratus unchained", "https://picsum.photos/id/237/250", "John Doe", new BookStatusDTO(true, "NEW")),
			new BookSummaryDTO(3, "Captain underpants: Finally peace", "https://picsum.photos/id/237/250", "John Doe", new BookStatusDTO(true, "NEW"))
        );
        ReservationDTO reservation = new ReservationDTO(1,1,"John",LocalDate.of(2000,2,1),LocalDate.of(2000,2,11), List.of(1,2,3));
        reservation.setContent(reservationDetails);

        when(reservationService.findReservationDetails(anyInt())).thenReturn(reservation);

        mockMvc.perform(get("/api/reservations/view/1?page=0&size=2&sort=name,asc")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "John",roles = {"EMPLOYEE","ADMIN"})
    void tryGettingReservationDetailsAsEmployeeOrAdminRole() throws Exception {
        List<BookSummaryDTO> reservationDetails = List.of(
            new BookSummaryDTO(1, "Captain underpants", "https://picsum.photos/id/237/250", "John Doe", new BookStatusDTO(true, "NEW")),
			new BookSummaryDTO(2, "Captain underpants: Dr Kratus unchained", "https://picsum.photos/id/237/250", "John Doe", new BookStatusDTO(true, "NEW")),
			new BookSummaryDTO(3, "Captain underpants: Finally peace", "https://picsum.photos/id/237/250", "John Doe", new BookStatusDTO(true, "NEW"))
        );
        ReservationDTO reservation = new ReservationDTO(1,1,"John",LocalDate.of(2000,2,1),LocalDate.of(2000,2,11), List.of(1,2,3));
        reservation.setContent(reservationDetails);

        when(reservationService.findReservationDetails(anyInt())).thenReturn(reservation);

        mockMvc.perform(get("/api/reservations/view/1?page=0&size=2&sort=name,asc"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(3))
        .andExpect(jsonPath("$.content[0].title").value("Captain underpants"))
        .andExpect(jsonPath("$.content[1].title").value("Captain underpants: Dr Kratus unchained"))
        .andExpect(jsonPath("$.content[2].title").value("Captain underpants: Finally peace"));
    }

    @Test
    @WithMockUser(username = "John", roles = {"CUSTOMER","EMPLOYEE","ADMIN"})
    void tryAddingNewReservation() throws Exception {
        ReservationDTO newReservation = new ReservationDTO(1,1,"John",LocalDate.of(2000,2,1),LocalDate.of(2000,2,11), List.of(1,2,3));

        when(reservationService.addNewReservation(any(ReservationDTO.class))).thenReturn(newReservation);

        mockMvc.perform(post("/api/reservations/add")
        .contentType("application/json")
        .content("""
            {
                "id": 1,
                "userID": 1,
                "beginDate": "2000-02-01",
                "endDate": "2000-02-11",
                "bookIds": [1,2,3]
            }
            """))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.beginDate").value("2000-02-01"))
        .andExpect(jsonPath("$.endDate").value("2000-02-11"));
    }

    @Test
    @WithMockUser(username = "John", roles = {"CUSTOMER"})
    void tryUpdatingReservationAsCustomer() throws Exception {
        ReservationDTO reservation = new ReservationDTO(1,1,"John",LocalDate.of(2000,2,1),LocalDate.of(2000,2,11), List.of(1,2,3));

        when(reservationService.updateReservation(anyInt(), any(ReservationDTO.class))).thenReturn(reservation);

        mockMvc.perform(put("/api/reservations/update/1").contentType("application/json")
        .content("""
            {
                "beginDate": "2001-02-01",
                "endDate": "2001-02-11"
            }
            """))
        .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "John", roles = {"EMPLOYEE","ADMIN"})
    void tryUpdatingReservationAsEmployeeOrAdmin() throws Exception {
        ReservationDTO reservation = new ReservationDTO(1,1,"John",LocalDate.of(2001,2,1),LocalDate.of(2001,2,11), List.of(1,2,3));

        when(reservationService.updateReservation(anyInt(), any(ReservationDTO.class))).thenReturn(reservation);

        mockMvc.perform(put("/api/reservations/update/1")
        .contentType("application/json")
        .content("""
            {
                "beginDate": "2001-02-01",
                "endDate": "2001-02-11"
            }
            """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.beginDate").value("2001-02-01"))
        .andExpect(jsonPath("$.endDate").value("2001-02-11"));
    }

    @Test
    @WithMockUser(username = "John", roles = {"CUSTOMER"})
    void tryDeletingReservationAsCustomer() throws Exception {
        ReservationDTO reservation = new ReservationDTO(1,1,"John",LocalDate.of(2000,2,1),LocalDate.of(2000,2,11), List.of(1,2,3));

        when(reservationService.removeReservation(anyInt())).thenReturn(reservation);

        mockMvc.perform(delete("/api/reservations/remove/1")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "John", roles = {"EMPLOYEE","ADMIN"})
    void tryDeletingReservationAsEmployeeOrAdmin() throws Exception {
        ReservationDTO reservation = new ReservationDTO(1,1,"John",LocalDate.of(2000,2,1),LocalDate.of(2000,2,11), List.of(1,2,3));

        when(reservationService.removeReservation(anyInt())).thenReturn(reservation);

        mockMvc.perform(delete("/api/reservations/remove/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.beginDate").value("2000-02-01"))
        .andExpect(jsonPath("$.endDate").value("2000-02-11"));
    }
}
