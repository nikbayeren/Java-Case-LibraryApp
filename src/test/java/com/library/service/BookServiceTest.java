package com.library.service;

import com.library.dto.BookDTO;
import com.library.dto.BookResponseDTO;
import com.library.entity.Author;
import com.library.entity.Book;
import com.library.entity.Publisher;
import com.library.repository.AuthorRepository;
import com.library.repository.BookRepository;
import com.library.repository.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private PublisherRepository publisherRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBook() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Test Book");
        bookDTO.setIsbn13("9789750502158");
        bookDTO.setPublishYear(2023);
        bookDTO.setPrice(29.99);
        bookDTO.setAuthorId(1L);
        bookDTO.setPublisherId(1L);

        Author author = new Author();
        author.setId(1L);
        author.setName("Test Author");
        Publisher publisher = new Publisher();
        publisher.setId(1L);
        publisher.setName("Test Publisher");
        Book book = new Book();
        book.setId(1L);
        book.setTitle(bookDTO.getTitle());
        book.setIsbn13(bookDTO.getIsbn13());
        book.setPublishYear(bookDTO.getPublishYear());
        book.setPrice(bookDTO.getPrice());
        book.setAuthor(author);
        book.setPublisher(publisher);

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookResponseDTO response = bookService.createBook(bookDTO);
        assertNotNull(response);
        assertEquals("Test Book", response.getTitle());
        assertEquals("9789750502158", response.getIsbn13());
    }

    @Test
    void testGetAllBooks() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setIsbn13("9789750502158");
        book.setPublishYear(2023);
        book.setPrice(29.99);
        Author author = new Author();
        author.setId(1L);
        book.setAuthor(author);
        Publisher publisher = new Publisher();
        publisher.setId(1L);
        book.setPublisher(publisher);
        List<Book> books = List.of(book);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(books, pageable, 1);
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        Page<BookResponseDTO> result = bookService.getAllBooks(pageable);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Book", result.getContent().get(0).getTitle());
    }
} 