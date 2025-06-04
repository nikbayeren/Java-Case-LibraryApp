package com.library.controller;

import com.library.dto.PublisherDTO;
import com.library.dto.PublisherResponseDTO;
import com.library.service.PublisherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publishers")
@Tag(name = "Publisher Controller", description = "Yayınevi işlemleri için API endpoints")
public class PublisherController {

    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @PostMapping
    @Operation(summary = "Yeni yayınevi ekle")
    public ResponseEntity<PublisherResponseDTO> createPublisher(@RequestBody PublisherDTO publisherDTO) {
        return ResponseEntity.ok(publisherService.createPublisher(publisherDTO));
    }

    @GetMapping
    @Operation(summary = "Tüm yayınevlerini listele")
    public ResponseEntity<List<PublisherResponseDTO>> getAllPublishers() {
        return ResponseEntity.ok(publisherService.getAllPublishers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID'ye göre yayınevi getir")
    public ResponseEntity<PublisherResponseDTO> getPublisherById(@PathVariable Long id) {
        return ResponseEntity.ok(publisherService.getPublisherById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Yayınevi bilgilerini güncelle")
    public ResponseEntity<PublisherResponseDTO> updatePublisher(@PathVariable Long id, @RequestBody PublisherDTO publisherDTO) {
        return ResponseEntity.ok(publisherService.updatePublisher(id, publisherDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Yayınevi sil")
    public ResponseEntity<Void> deletePublisher(@PathVariable Long id) {
        publisherService.deletePublisher(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/first2-with-books-and-authors")
    @Operation(summary = "İlk 2 yayınevini ve kitap/yazarlarını listele")
    public ResponseEntity<List<PublisherResponseDTO>> getFirst2PublishersWithBooksAndAuthors() {
        return ResponseEntity.ok(publisherService.getFirst2PublishersWithBooksAndAuthors());
    }
} 