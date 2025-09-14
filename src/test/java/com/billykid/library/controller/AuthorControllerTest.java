package com.billykid.library.controller;

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

import com.billykid.library.config.SpringSecurityConfiguration;
import com.billykid.library.controller.AuthorController;
import com.billykid.library.service.AuthorService;
import com.billykid.library.service.CustomUserDetailsService;
import com.billykid.library.utils.DTO.AuthorDTO;
import com.billykid.library.utils.properties.CorsProperties;

@Profile("test")
@WebMvcTest(controllers = AuthorController.class)
@EnableConfigurationProperties(CorsProperties.class)
@AutoConfigureMockMvc(addFilters = true)
@Import(SpringSecurityConfiguration.class)
public class AuthorControllerTest {
    @Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private AuthorService authorService;

	@MockitoBean
	private CustomUserDetailsService userService;

	@Test
    void shouldReturnAuthorsByNameWithPagination() throws Exception {
        // Given
        List<AuthorDTO> authors = List.of(
            new AuthorDTO(1,"John Parry","The writer of the best seller", LocalDate.of( 1985 , 1 , 1 )),
			new AuthorDTO(2,"Johnny Cash","The writer of another best seller", LocalDate.of( 1990 , 10 , 11 ))
        );

        when(authorService.findAuthorsByName(eq("john"), any(Pageable.class)))
            .thenReturn(authors);

        mockMvc.perform(get("/api/authors/name/john?page=0&size=2&sort=name,asc"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].name").value("John Parry"))
        .andExpect(jsonPath("$[1].name").value("Johnny Cash"));
    }

	@Test
	@WithMockUser(username = "John", roles = {"CUSTOMER"})
	void tryAddingNewAuthorAsCustomerRole() throws Exception {
		mockMvc.perform(post("/api/authors/add")
                .contentType("application/json")
                .content("""
                    {
                        "name": "Billy",
                        "bio": "New book and new bio",
                        "dateOfBirth": "1892-01-03"
                    }
                    """))
                .andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "John", roles = {"EMPLOYEE", "ADMIN"})
	void tryAddingNewAuthorAsEmployeeOrAdminRole() throws Exception {
		AuthorDTO mockAuthor = new AuthorDTO(1,"Billy","The writer of the best seller", LocalDate.of( 1892 , 1 , 3 ));
        when(authorService.addNewAuthor(any(AuthorDTO.class))).thenReturn(mockAuthor);

        // Effectuer la requête POST et vérifier la réponse
        mockMvc.perform(post("/api/authors/add")
                .contentType("application/json")
                .content("""
                    {
                        "name": "Billy",
                        "bio": "New book and new bio",
                        "dateOfBirth": "1892-01-03"
                    }
                    """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Billy"));
	}

    @Test
	@WithMockUser(username = "John", roles = {"CUSTOMER"})
	void tryUpdatingAuthorAsCustomerRole() throws Exception {

        // Effectuer la requête PUT et vérifier la réponse
        mockMvc.perform(put("/api/authors/update/1")
                .contentType("application/json")
                .content("""
                    {
                        "bio": "New description",
                        "dateOfBirth": "1893-01-03"
                    }
                    """))
                .andExpect(status().isForbidden());
	}

    @Test
	@WithMockUser(username = "John", roles = {"EMPLOYEE", "ADMIN"})
	void tryUpdatingAuthorAsEmployeeOrAdminRole() throws Exception {
		AuthorDTO mockAuthor = new AuthorDTO(1,"Billy","New description", LocalDate.of( 1893 , 1 , 3 ));
        when(authorService.updateAuthor(anyInt(),any(AuthorDTO.class))).thenReturn(mockAuthor);

        // Effectuer la requête PUT et vérifier la réponse
        mockMvc.perform(put("/api/authors/update/1")
                .contentType("application/json")
                .content("""
                    {
                        "bio": "New description",
                        "dateOfBirth": "1893-01-03"
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bio").value("New description"))
                .andExpect(jsonPath("$.dateOfBirth").value("1893-01-03"));
	}

    @Test
	@WithMockUser(username = "John", roles = {"CUSTOMER"})
	void tryDeletingAuthorAsCustomerRole() throws Exception {

        // Effectuer la requête DELETE et vérifier la réponse
        mockMvc.perform(delete("/api/authors/remove/1")).andExpect(status().isForbidden());
	}

    @Test
	@WithMockUser(username = "John", roles = {"EMPLOYEE", "ADMIN"})
	void tryDeletingAuthorAsEmployeeOrAdminRole() throws Exception {
		AuthorDTO deletedMockAuthor = new AuthorDTO(1,"Billy","New description", LocalDate.of( 1893 , 1 , 3 ));
        when(authorService.removeAuthor(anyInt())).thenReturn(deletedMockAuthor);

        // Effectuer la requête DELETE et vérifier la réponse
        mockMvc.perform(delete("/api/authors/remove/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Billy"))
                .andExpect(jsonPath("$.bio").value("New description"))
                .andExpect(jsonPath("$.dateOfBirth").value("1893-01-03"));
	}
}
