package com.library.controller;

import com.library.dto.*;
import com.library.service.BookService;
import com.library.client.GoogleBooksClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @MockBean
    private GoogleBooksClient googleBooksClient;

    @Test
    void testGetAllBooks() throws Exception {
        // Given
        List<BookResponseDTO> books = new ArrayList<>();
        Page<BookResponseDTO> page = new PageImpl<>(books, PageRequest.of(0, 10), 0);
        when(bookService.getAllBooks(any(Pageable.class))).thenReturn(page);

        // When & Then
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.totalPages").value(0))
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    void testCreateBook() throws Exception {
        // Given
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Test Book");
        bookDTO.setIsbn13("9789750502158");
        bookDTO.setPublishYear(2023);
        bookDTO.setPrice(29.99);
        bookDTO.setAuthorId(1L);
        bookDTO.setPublisherId(1L);

        BookResponseDTO responseDTO = new BookResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setTitle(bookDTO.getTitle());
        responseDTO.setIsbn13(bookDTO.getIsbn13());
        responseDTO.setPublishYear(bookDTO.getPublishYear());
        responseDTO.setPrice(bookDTO.getPrice());
        responseDTO.setAuthorId(bookDTO.getAuthorId());
        responseDTO.setPublisherId(bookDTO.getPublisherId());

        when(bookService.createBook(any(BookDTO.class))).thenReturn(responseDTO);

        // When & Then
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Test Book"));
    }

    @Test
    void testSearchBooks() throws Exception {
        // Given
        List<BookResponseDTO> books = new ArrayList<>();
        when(bookService.searchBooks(any())).thenReturn(books);

        // When & Then
        mockMvc.perform(get("/api/books/search").param("query", "test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void testSearchGoogleBooks() throws Exception {
        // Given
        Map<String, Object> mockResponse = Map.of(
            "items", List.of(Map.of(
                "volumeInfo", Map.of(
                    "title", "Test Book",
                    "authors", List.of("Test Author"),
                    "publisher", "Test Publisher",
                    "industryIdentifiers", List.of(Map.of(
                        "type", "ISBN_13",
                        "identifier", "9789750502158"
                    ))
                ),
                "saleInfo", Map.of(
                    "listPrice", Map.of(
                        "amount", 29.99
                    )
                )
            ))
        );

        when(googleBooksClient.searchBooks(any())).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(get("/api/books/search-google").param("query", "test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].title").value("Test Book"))
                .andExpect(jsonPath("$.data[0].authorNameSurname").value("Test Author"))
                .andExpect(jsonPath("$.data[0].publisherName").value("Test Publisher"))
                .andExpect(jsonPath("$.data[0].isbn13").value("9789750502158"))
                .andExpect(jsonPath("$.data[0].price").value(29.99));
    }

    @Test
    void testGetBooksStartingWithA() throws Exception {
        List<BookResponseDTO> books = new ArrayList<>();
        BookResponseDTO book = new BookResponseDTO();
        book.setId(1L);
        book.setTitle("Aşk ve Gurur");
        books.add(book);
        when(bookService.getBooksStartingWithA()).thenReturn(books);

        mockMvc.perform(get("/api/books/starts-with-a"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].title").value("Aşk ve Gurur"));
    }

    @Test
    void testCreateBookWithNames() throws Exception {
        BookCreateRequest request = new BookCreateRequest();
        request.setTitle("Clean Code");
        request.setPrice(45.0);
        request.setIsbn13("9780132350884");
        request.setPublisherName("Prentice Hall");
        request.setAuthorNameSurname("Robert C. Martin");
        request.setPublishYear(2008);

        BookResponseDTO responseDTO = new BookResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setTitle(request.getTitle());
        responseDTO.setIsbn13(request.getIsbn13());
        responseDTO.setPublishYear(request.getPublishYear());
        responseDTO.setPrice(request.getPrice());

        when(bookService.createBookWithNames(any(BookCreateRequest.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/books/create-with-names")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("Clean Code"));
    }
} 