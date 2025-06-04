package com.library.controller;

import com.library.dto.*;
import com.library.service.BookService;
import com.library.client.GoogleBooksClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Book Controller", description = "Kitap işlemleri için API endpoints")
public class BookController {

    private final BookService bookService;
    private final GoogleBooksClient googleBooksClient;

    public BookController(BookService bookService, GoogleBooksClient googleBooksClient) {
        this.bookService = bookService;
        this.googleBooksClient = googleBooksClient;
    }

    @PostMapping
    @Operation(summary = "Yeni kitap ekle", description = "Sisteme yeni bir kitap ekler")
    public ResponseEntity<ApiResponse<BookResponseDTO>> createBook(@RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok(ApiResponse.success(bookService.createBook(bookDTO)));
    }

    @PostMapping("/create-with-names")
    @Operation(summary = "Yazar ve yayınevi ismiyle kitap ekle", description = "Kitap eklerken yazar ve yayınevi ismiyle ekleme")
    public ResponseEntity<ApiResponse<BookResponseDTO>> createBookWithNames(@RequestBody BookCreateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(bookService.createBookWithNames(request)));
    }

    @GetMapping
    @Operation(summary = "Tüm kitapları listele (sayfalı)", description = "Sayfalı olarak tüm kitapları listeler")
    public ResponseEntity<ApiResponse<List<BookResponseDTO>>> getAllBooks(Pageable pageable) {
        Page<BookResponseDTO> books = bookService.getAllBooks(pageable);
        return ResponseEntity.ok(ApiResponse.paginated(
            books.getContent(),
            books.getTotalPages(),
            books.getTotalElements()
        ));
    }

    @GetMapping("/all")
    @Operation(summary = "Tüm kitapları listele (tam liste)", description = "Sistemdeki tüm kitapları döner")
    public ResponseEntity<ApiResponse<List<BookResponseDTO>>> getAllBooksNoPaging() {
        return ResponseEntity.ok(ApiResponse.success(bookService.getAllBooksNoPaging()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID'ye göre kitap getir", description = "Belirtilen ID'ye sahip kitabın detaylarını getirir")
    public ResponseEntity<ApiResponse<BookResponseDTO>> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(bookService.getBookById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Kitap bilgilerini güncelle", description = "Belirtilen ID'ye sahip kitabın bilgilerini günceller")
    public ResponseEntity<ApiResponse<BookResponseDTO>> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok(ApiResponse.success(bookService.updateBook(id, bookDTO)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Kitap sil", description = "Belirtilen ID'ye sahip kitabı sistemden siler")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Kitap başarıyla silindi"));
    }

    @GetMapping("/author/{authorId}")
    @Operation(summary = "Yazara göre kitapları listele", description = "Belirtilen yazara ait tüm kitapları listeler")
    public ResponseEntity<ApiResponse<List<BookResponseDTO>>> getBooksByAuthor(@PathVariable Long authorId) {
        return ResponseEntity.ok(ApiResponse.success(bookService.getBooksByAuthor(authorId)));
    }

    @GetMapping("/publisher/{publisherId}")
    @Operation(summary = "Yayınevine göre kitapları listele", description = "Belirtilen yayınevine ait tüm kitapları listeler")
    public ResponseEntity<ApiResponse<List<BookResponseDTO>>> getBooksByPublisher(@PathVariable Long publisherId) {
        return ResponseEntity.ok(ApiResponse.success(bookService.getBooksByPublisher(publisherId)));
    }

    @GetMapping("/search")
    @Operation(summary = "Kitap ara", description = "Başlık veya ISBN'e göre kitap araması yapar")
    public ResponseEntity<ApiResponse<List<BookResponseDTO>>> searchBooks(@RequestParam String query) {
        return ResponseEntity.ok(ApiResponse.success(bookService.searchBooks(query)));
    }

    @GetMapping("/year/{year}")
    @Operation(summary = "Yıla göre kitapları listele", description = "Belirtilen yıldan sonra yayınlanan kitapları listeler")
    public ResponseEntity<ApiResponse<List<BookResponseDTO>>> getBooksByYear(@PathVariable Integer year) {
        return ResponseEntity.ok(ApiResponse.success(bookService.getBooksByYear(year)));
    }

    @GetMapping("/price")
    @Operation(summary = "Fiyat aralığına göre kitapları listele", description = "Belirtilen fiyat aralığındaki kitapları listeler")
    public ResponseEntity<ApiResponse<List<BookResponseDTO>>> getBooksByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {
        return ResponseEntity.ok(ApiResponse.success(bookService.getBooksByPriceRange(minPrice, maxPrice)));
    }

    @GetMapping("/search-google")
    @Operation(summary = "Google Books API'den kitap ara", description = "Google Books API üzerinden kitap araması yapar")
    public ResponseEntity<ApiResponse<List<GoogleBookResponse>>> searchGoogleBooks(@RequestParam String query) {
        Map<String, Object> response = googleBooksClient.searchBooks(query);
        List<GoogleBookResponse> books = mapGoogleBooksResponse(response);
        return ResponseEntity.ok(ApiResponse.success(books));
    }

    @GetMapping("/starts-with-a")
    @Operation(summary = "A harfiyle başlayan kitapları listele")
    public ResponseEntity<ApiResponse<List<BookResponseDTO>>> getBooksStartingWithA() {
        return ResponseEntity.ok(ApiResponse.success(bookService.getBooksStartingWithA()));
    }

    @GetMapping("/publishers/books")
    @Operation(summary = "İki yayınevinin kitaplarını ve yazarlarını listele", 
               description = "Belirtilen iki yayınevine ait kitapları ve yazarları listeler")
    public ResponseEntity<ApiResponse<List<BookResponseDTO>>> getBooksByPublishers(
            @RequestParam List<Long> publisherIds) {
        try {
            return ResponseEntity.ok(ApiResponse.success(
                bookService.getBooksByPublishers(publisherIds)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage()));
        }
    }

    private List<GoogleBookResponse> mapGoogleBooksResponse(Map<String, Object> response) {
        List<GoogleBookResponse> result = new ArrayList<>();
        List<Map<String, Object>> items = (List<Map<String, Object>>) response.get("items");
        if (items == null) return result;
        
        for (Map<String, Object> item : items) {
            Map<String, Object> volumeInfo = (Map<String, Object>) item.get("volumeInfo");
            Map<String, Object> saleInfo = (Map<String, Object>) item.get("saleInfo");
            GoogleBookResponse dto = new GoogleBookResponse();
            
            dto.setTitle(volumeInfo != null ? (String) volumeInfo.get("title") : null);
            
            if (volumeInfo != null && volumeInfo.get("authors") != null) {
                List<String> authors = (List<String>) volumeInfo.get("authors");
                if (!authors.isEmpty()) dto.setAuthorNameSurname(authors.get(0));
            }
            
            dto.setPublisherName(volumeInfo != null ? (String) volumeInfo.get("publisher") : null);
            
            if (volumeInfo != null && volumeInfo.get("industryIdentifiers") != null) {
                List<Map<String, String>> ids = (List<Map<String, String>>) volumeInfo.get("industryIdentifiers");
                for (Map<String, String> idObj : ids) {
                    if ("ISBN_13".equals(idObj.get("type"))) {
                        dto.setIsbn13(idObj.get("identifier"));
                        break;
                    }
                }
            }
            
            if (saleInfo != null && saleInfo.get("listPrice") != null) {
                Map<String, Object> listPrice = (Map<String, Object>) saleInfo.get("listPrice");
                if (listPrice.get("amount") != null) {
                    dto.setPrice(Double.valueOf(listPrice.get("amount").toString()));
                }
            }
            
            result.add(dto);
        }
        return result;
    }
} 