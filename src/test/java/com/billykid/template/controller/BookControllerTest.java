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
import com.billykid.template.service.BookService;
import com.billykid.template.service.CustomUserDetailsService;
import com.billykid.template.utils.DTO.PagedResponse;
import com.billykid.template.utils.DTO.book.BookDetailsDTO;
import com.billykid.template.utils.DTO.book.BookStatusDTO;
import com.billykid.template.utils.DTO.book.BookSummaryDTO;
import com.billykid.template.utils.parameters.BookParametersObject;
import com.billykid.template.utils.properties.CorsProperties;

@Profile("test")
@WebMvcTest(controllers = BookController.class)
@EnableConfigurationProperties(CorsProperties.class)
@AutoConfigureMockMvc(addFilters = true)
@Import(SpringSecurityConfiguration.class)
public class BookControllerTest {
    @Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private BookService bookService;

	@MockitoBean
	private CustomUserDetailsService userService;

	@Test
	void shouldReturnBookByTitleWithPagination() throws Exception {
		// Given
        List<BookSummaryDTO> books = List.of(
            new BookSummaryDTO(1, "Captain underpants", "https://picsum.photos/id/237/250", "John Doe", new BookStatusDTO(true, "NEW")),
			new BookSummaryDTO(2, "Captain underpants: Dr Kratus unchained", "https://picsum.photos/id/237/250", "John Doe", new BookStatusDTO(true, "NEW")),
			new BookSummaryDTO(3, "Captain underpants: Finally peace", "https://picsum.photos/id/237/250", "John Doe", new BookStatusDTO(true, "NEW"))
        );

        when(bookService.findBooksByTitle(eq("Captain"), any(Pageable.class))).thenReturn(books);
        // When & Then
        mockMvc.perform(get("/api/books/title/Captain?page=0&size=2&sort=name,asc"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(3))
            .andExpect(jsonPath("$[0].title").value("Captain underpants"))
            .andExpect(jsonPath("$[1].title").value("Captain underpants: Dr Kratus unchained"))
			.andExpect(jsonPath("$[2].title").value("Captain underpants: Finally peace"));
	}

	@Test
	void shouldReturnBookByAuthorWithPagination() throws Exception {
		// Given
        List<BookSummaryDTO> books = List.of(
            new BookSummaryDTO(1, "Captain underpants", "https://picsum.photos/id/237/250", "John Doe", new BookStatusDTO(true, "NEW")),
			new BookSummaryDTO(2, "Captain underpants: Dr Kratus unchained", "https://picsum.photos/id/237/250", "John Doe", new BookStatusDTO(true, "NEW")),
			new BookSummaryDTO(3, "Captain underpants: Finally peace", "https://picsum.photos/id/237/250", "John Doe", new BookStatusDTO(true, "NEW"))
        );

        when(bookService.findBooksByAuthor(eq("Bobby"), any(Pageable.class))).thenReturn(books);
        // When & Then
        mockMvc.perform(get("/api/books/author/Bobby?page=0&size=2&sort=name,asc"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(3))
            .andExpect(jsonPath("$[0].title").value("Captain underpants"))
            .andExpect(jsonPath("$[1].title").value("Captain underpants: Dr Kratus unchained"))
			.andExpect(jsonPath("$[2].title").value("Captain underpants: Finally peace"));
	}

	@Test
	void shouldReturnBookByGenresWithPagination() throws Exception {
		// Given
        List<BookSummaryDTO> books = List.of(
            new BookSummaryDTO(1, "Captain underpants", "https://picsum.photos/id/237/250", "John Doe", new BookStatusDTO(true, "NEW")),
			new BookSummaryDTO(2, "Captain underpants: Dr Kratus unchained", "https://picsum.photos/id/237/250", "John Doe", new BookStatusDTO(true, "NEW")),
			new BookSummaryDTO(3, "Captain underpants: Finally peace", "https://picsum.photos/id/237/250", "John Doe", new BookStatusDTO(true, "NEW"))
        );

        when(bookService.findBooksByGenres(eq(List.of("Adventure","Comedy")), any(Pageable.class))).thenReturn(books);
        // When & Then
        mockMvc.perform(get("/api/books/genres/Adventure, Comedy?page=0&size=2&sort=name,asc"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(3))
            .andExpect(jsonPath("$[0].title").value("Captain underpants"))
            .andExpect(jsonPath("$[1].title").value("Captain underpants: Dr Kratus unchained"))
			.andExpect(jsonPath("$[2].title").value("Captain underpants: Finally peace"));
	}

	@Test
	void shouldReturnBookByReservedWithPagination() throws Exception {
		// Given
        List<BookSummaryDTO> books = List.of(
           new BookSummaryDTO(1, "Captain underpants", "https://picsum.photos/id/237/250", "John Doe", new BookStatusDTO(true, "NEW")),
			new BookSummaryDTO(2, "Captain underpants: Dr Kratus unchained", "https://picsum.photos/id/237/250", "John Doe", new BookStatusDTO(true, "NEW")),
			new BookSummaryDTO(3, "Captain underpants: Finally peace", "https://picsum.photos/id/237/250", "John Doe", new BookStatusDTO(true, "NEW"))
        );

        when(bookService.findBooksByAvailable(eq(true), any(Pageable.class))).thenReturn(books);
        // When & Then
        mockMvc.perform(get("/api/books/reserved/true?page=0&size=2&sort=name,asc"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(3))
            .andExpect(jsonPath("$[0].title").value("Captain underpants"))
            .andExpect(jsonPath("$[1].title").value("Captain underpants: Dr Kratus unchained"))
			.andExpect(jsonPath("$[2].title").value("Captain underpants: Finally peace"));
	}

	@Test
	void shouldReturnBookByFiltersWithPagination() throws Exception {
		// Given
        List<BookSummaryDTO> books = List.of(
            new BookSummaryDTO(1, "Captain underpants", "https://picsum.photos/id/237/250", "John Doe", new BookStatusDTO(true, "NEW")),
			new BookSummaryDTO(2, "Captain underpants: Dr Kratus unchained", "https://picsum.photos/id/237/250", "John Doe", new BookStatusDTO(true, "NEW")),
			new BookSummaryDTO(3, "Captain underpants: Finally peace", "https://picsum.photos/id/237/250", "John Doe", new BookStatusDTO(true, "NEW"))
        );
        PagedResponse<BookSummaryDTO> pagedResponse = new PagedResponse<>(
            books,
            0,
            2,
            3,
            2
        );


        when(bookService.findBooksByQueryParams(any(BookParametersObject.class), any(Pageable.class))).thenReturn(pagedResponse);
        // When & Then
        mockMvc.perform(get("/api/books?title=Captain&author=Bobby&genres=Adventure, Comedy&isReserved=false&page=0&size=2&sort=name,asc"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()").value(3))
            .andExpect(jsonPath("$.content[0].title").value("Captain underpants"))
            .andExpect(jsonPath("$.content[1].title").value("Captain underpants: Dr Kratus unchained"))
			.andExpect(jsonPath("$.content[2].title").value("Captain underpants: Finally peace"));
	}

    @Test
    void tryGettingBookDetails() throws Exception {
        BookDetailsDTO bookDetails = new BookDetailsDTO(1, "Captain underpants", "This is a story about a funny hero", "https://picsum.photos/id/237/250", List.of("Adventure","Comedy"), new BookStatusDTO(true, "NEW"),1,"John Doe", LocalDate.ofYearDay(2015, 5), 1, 100);
        when(bookService.findBookDetails(anyInt())).thenReturn(bookDetails);
        mockMvc.perform(get("/api/books/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Captain underpants"))
            .andExpect(jsonPath("$.description").value("This is a story about a funny hero"))
            .andExpect(jsonPath("$.genres[0]").value("Adventure"))
            .andExpect(jsonPath("$.genres[1]").value("Comedy"));
    }

	@Test
	@WithMockUser(username = "John", roles = {"CUSTOMER"})
	void tryAddingBookAsCustomerRole() throws Exception {
		BookDetailsDTO book = new BookDetailsDTO(1, "Captain underpants", "This is a story about a funny hero", "https://picsum.photos/id/237/250", List.of("Adventure","Comedy"), new BookStatusDTO(true, "NEW"),1,"John Doe", LocalDate.ofYearDay(2015, 5), 1, 100);
		when(bookService.addNewBook(any(BookDetailsDTO.class))).thenReturn(book);
        // Effectuer la requête POST et vérifier la réponse
        mockMvc.perform(post("/api/books/add")
                .contentType("application/json")
                .content("""
                    {
                        "title": "Captain underpants",
                        "description": "This is a story about a funny hero",
						"bookCoverUrl": "https://picsum.photos/id/237/250",
						"genres": ["Adventure","Comedy"],
						"status": {
							"available": true,
							"condition": "NEW"
						},
						"authorId": 1,
						"volume": 1
                    }
                    """))
                .andExpect(status().isForbidden());
	}

    @Test
	@WithMockUser(username = "John", roles = {"EMPLOYEE", "ADMIN"})
	void tryAddingBookAsEmployeeOrAdminRole() throws Exception {
		BookDetailsDTO book = new BookDetailsDTO(1, "Captain underpants", "This is a story about a funny hero", "https://picsum.photos/id/237/250", List.of("Adventure","Comedy"), new BookStatusDTO(true, "NEW"),1,"John Doe", LocalDate.ofYearDay(2015, 5), 1, 100);
		when(bookService.addNewBook(any(BookDetailsDTO.class))).thenReturn(book);
        // Effectuer la requête POST et vérifier la réponse
        mockMvc.perform(post("/api/books/add")
                .contentType("application/json")
                .content("""
                    {
                        "title": "Captain underpants",
                        "description": "This is a story about a funny hero",
						"bookCoverUrl": "https://picsum.photos/id/237/250",
						"genres": ["Adventure","Comedy"],
						"status": {
							"available": true,
							"condition": "NEW"
						},
						"authorId": 1,
						"volume": 1
                    }
                    """))
                .andExpect(status().isCreated())
				.andExpect(jsonPath("$.title").value("Captain underpants"))
				.andExpect(jsonPath("$.description").value("This is a story about a funny hero"));
	}

	@Test
	@WithMockUser(username = "John", roles = {"CUSTOMER"})
	void tryUpdatingBookAsCustomerRole() throws Exception {
		BookDetailsDTO book = new BookDetailsDTO(1, "Captain underpants", "This is a story about a funny hero", "https://picsum.photos/id/237/250", List.of("Adventure","Comedy"), new BookStatusDTO(true, "NEW"),1,"John Doe", LocalDate.ofYearDay(2015, 5), 1, 100);
		when(bookService.updateBook(anyInt(),any(BookDetailsDTO.class))).thenReturn(book);
        // Effectuer la requête PUT et vérifier la réponse
        mockMvc.perform(put("/api/books/update/1")
                .contentType("application/json")
                .content("""
                    {
                        "title": "Captain underpants",
                        "description": "This is a story about a wacky hero"
                    }
                    """))
                .andExpect(status().isForbidden());
	}

    @Test
	@WithMockUser(username = "John", roles = {"EMPLOYEE", "ADMIN"})
	void tryUpdatingBookAsEmployeeOrAdminRole() throws Exception {
		BookDetailsDTO book = new BookDetailsDTO(1, "Captain underpants", "This is a story about a funny hero", "https://picsum.photos/id/237/250", List.of("Adventure","Comedy"), new BookStatusDTO(true, "NEW"), 1, "John Doe", LocalDate.ofYearDay(2013, 5), 1, 199);
		when(bookService.updateBook(anyInt(),any(BookDetailsDTO.class))).thenReturn(book);
        // Effectuer la requête PUT et vérifier la réponse
        mockMvc.perform(put("/api/books/update/1")
                .contentType("application/json")
                .content("""
                    {
                        "title": "Captain underpant",
                        "description": "This is a story about a wacky hero"
                    }
                    """))
                .andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("Captain underpants"))
				.andExpect(jsonPath("$.description").value("This is a story about a funny hero"));
	}

	@Test
	@WithMockUser(username = "John", roles = {"CUSTOMER"})
	void tryDeletingBookAsCustomerRole() throws Exception {
		BookDetailsDTO book = new BookDetailsDTO(1, "Captain underpants", "This is a story about a funny hero", "https://picsum.photos/id/237/250", List.of("Adventure","Comedy"), new BookStatusDTO(true, "NEW"),1,"John Doe", LocalDate.ofYearDay(2015, 5), 1, 100);
		when(bookService.removeBook(anyInt())).thenReturn(book);
        // Effectuer la requête PUT et vérifier la réponse
        mockMvc.perform(delete("/api/books/remove/1")).andExpect(status().isForbidden());
	}

    @Test
	@WithMockUser(username = "John", roles = {"EMPLOYEE", "ADMIN"})
	void tryDeletingBookAsEmployeeOrAdminRole() throws Exception {
		BookDetailsDTO book = new BookDetailsDTO(1, "Captain underpants", "This is a story about a wacky hero", "https://picsum.photos/id/237/250", List.of("Adventure","Comedy"), new BookStatusDTO(true, "NEW"),1,"John Doe", LocalDate.ofYearDay(2015, 5), 1, 100);
		when(bookService.removeBook(anyInt())).thenReturn(book);
        // Effectuer la requête PUT et vérifier la réponse
        mockMvc.perform(delete("/api/books/remove/1"))
                .andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("Captain underpants"))
				.andExpect(jsonPath("$.description").value("This is a story about a wacky hero"));
	}
}
