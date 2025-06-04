package com.library.service;

import com.library.dto.BookDTO;
import com.library.dto.BookResponseDTO;
import com.library.dto.BookCreateRequest;
import com.library.entity.Author;
import com.library.entity.Book;
import com.library.entity.Publisher;
import com.library.repository.AuthorRepository;
import com.library.repository.BookRepository;
import com.library.repository.PublisherRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, PublisherRepository publisherRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
    }

    @Transactional(readOnly = true)
    public Page<BookResponseDTO> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable).map(this::convertToResponseDTO);
    }

    @Transactional
    public BookResponseDTO createBook(BookDTO bookDTO) {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setIsbn13(bookDTO.getIsbn13());
        book.setPublishYear(bookDTO.getPublishYear());
        book.setPrice(bookDTO.getPrice());
        Author author = authorRepository.findById(bookDTO.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("Yazar bulunamadı: " + bookDTO.getAuthorId()));
        Publisher publisher = publisherRepository.findById(bookDTO.getPublisherId())
                .orElseThrow(() -> new EntityNotFoundException("Yayınevi bulunamadı: " + bookDTO.getPublisherId()));
        book.setAuthor(author);
        book.setPublisher(publisher);
        Book saved = bookRepository.save(book);
        return convertToResponseDTO(saved);
    }

    @Transactional
    public BookResponseDTO updateBook(Long id, BookDTO bookDTO) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kitap bulunamadı: " + id));
        book.setTitle(bookDTO.getTitle());
        book.setIsbn13(bookDTO.getIsbn13());
        book.setPublishYear(bookDTO.getPublishYear());
        book.setPrice(bookDTO.getPrice());
        Author author = authorRepository.findById(bookDTO.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("Yazar bulunamadı: " + bookDTO.getAuthorId()));
        Publisher publisher = publisherRepository.findById(bookDTO.getPublisherId())
                .orElseThrow(() -> new EntityNotFoundException("Yayınevi bulunamadı: " + bookDTO.getPublisherId()));
        book.setAuthor(author);
        book.setPublisher(publisher);
        Book updated = bookRepository.save(book);
        return convertToResponseDTO(updated);
    }

    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Kitap bulunamadı: " + id);
        }
        bookRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public BookResponseDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kitap bulunamadı: " + id));
        return convertToResponseDTO(book);
    }

    @Transactional(readOnly = true)
    public List<BookResponseDTO> getBooksByAuthor(Long authorId) {
        return bookRepository.findByAuthorId(authorId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookResponseDTO> getBooksByPublisher(Long publisherId) {
        return bookRepository.findByPublisherId(publisherId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookResponseDTO> searchBooks(String query) {
        return bookRepository.fullTextSearch(query).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookResponseDTO> getBooksByYear(Integer year) {
        return bookRepository.findByPublishYearGreaterThan(year).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookResponseDTO> getBooksByPriceRange(Double minPrice, Double maxPrice) {
        return bookRepository.findByPriceBetween(minPrice, maxPrice).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookResponseDTO createBookWithNames(BookCreateRequest request) {
        Author author = authorRepository.findByName(request.getAuthorNameSurname())
            .orElseGet(() -> {
                Author newAuthor = new Author();
                newAuthor.setName(request.getAuthorNameSurname());
                newAuthor.setBirthYear(request.getPublishYear());
                newAuthor.setCountry("Bilinmiyor");
                return authorRepository.save(newAuthor);
            });
        Publisher publisher = publisherRepository.findByName(request.getPublisherName())
            .orElseGet(() -> {
                Publisher newPublisher = new Publisher();
                newPublisher.setName(request.getPublisherName());
                newPublisher.setEstablishmentYear(request.getPublishYear());
                newPublisher.setAddress("Bilinmiyor");
                newPublisher.setPhone("Bilinmiyor");
                return publisherRepository.save(newPublisher);
            });
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setIsbn13(request.getIsbn13());
        book.setPublishYear(request.getPublishYear());
        book.setPrice(request.getPrice());
        book.setAuthor(author);
        book.setPublisher(publisher);
        Book saved = bookRepository.save(book);
        return convertToResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<BookResponseDTO> getBooksStartingWithA() {
        return bookRepository.findByTitleStartingWithIgnoreCase("A").stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookResponseDTO> getAllBooksNoPaging() {
        return bookRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookResponseDTO> getBooksByPublishers(List<Long> publisherIds) {
        if (publisherIds.size() != 2) {
            throw new IllegalArgumentException("Tam olarak 2 yayınevi ID'si gerekli");
        }
        return bookRepository.findByPublisherIdIn(publisherIds).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private BookResponseDTO convertToResponseDTO(Book book) {
        BookResponseDTO dto = new BookResponseDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setIsbn13(book.getIsbn13());
        dto.setPublishYear(book.getPublishYear());
        dto.setPrice(book.getPrice());
        dto.setAuthorId(book.getAuthor() != null ? book.getAuthor().getId() : null);
        dto.setPublisherId(book.getPublisher() != null ? book.getPublisher().getId() : null);
        return dto;
    }
} 