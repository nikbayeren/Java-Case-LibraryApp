package com.library.controller;

import com.library.dto.AuthorDTO;
import com.library.dto.AuthorResponseDTO;
import com.library.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@Tag(name = "Author Controller", description = "Yazar işlemleri için API endpoints")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    @Operation(summary = "Yeni yazar ekle")
    public ResponseEntity<AuthorResponseDTO> createAuthor(@RequestBody AuthorDTO authorDTO) {
        return ResponseEntity.ok(authorService.createAuthor(authorDTO));
    }

    @GetMapping
    @Operation(summary = "Tüm yazarları listele")
    public ResponseEntity<List<AuthorResponseDTO>> getAllAuthors() {
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID'ye göre yazar getir")
    public ResponseEntity<AuthorResponseDTO> getAuthorById(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Yazar bilgilerini güncelle")
    public ResponseEntity<AuthorResponseDTO> updateAuthor(@PathVariable Long id, @RequestBody AuthorDTO authorDTO) {
        return ResponseEntity.ok(authorService.updateAuthor(id, authorDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Yazarı sil")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.ok().build();
    }
} 