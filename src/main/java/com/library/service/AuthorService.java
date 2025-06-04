package com.library.service;

import com.library.dto.AuthorDTO;
import com.library.dto.AuthorResponseDTO;
import com.library.entity.Author;
import com.library.exception.AuthorNotFoundException;
import com.library.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Transactional
    public AuthorResponseDTO createAuthor(AuthorDTO authorDTO) {
        Author author = new Author();
        author.setName(authorDTO.getName());
        author.setBirthYear(authorDTO.getBirthYear());
        author.setCountry(authorDTO.getCountry());

        Author savedAuthor = authorRepository.save(author);
        return convertToResponseDTO(savedAuthor);
    }

    @Transactional(readOnly = true)
    public List<AuthorResponseDTO> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AuthorResponseDTO getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("Author not found with id: " + id));
        return convertToResponseDTO(author);
    }

    @Transactional
    public AuthorResponseDTO updateAuthor(Long id, AuthorDTO authorDTO) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("Author not found with id: " + id));

        author.setName(authorDTO.getName());
        author.setBirthYear(authorDTO.getBirthYear());
        author.setCountry(authorDTO.getCountry());

        Author updatedAuthor = authorRepository.save(author);
        return convertToResponseDTO(updatedAuthor);
    }

    @Transactional
    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new AuthorNotFoundException("Author not found with id: " + id);
        }
        authorRepository.deleteById(id);
    }

    private AuthorResponseDTO convertToResponseDTO(Author author) {
        AuthorResponseDTO dto = new AuthorResponseDTO();
        dto.setId(author.getId());
        dto.setName(author.getName());
        dto.setBirthYear(author.getBirthYear());
        dto.setCountry(author.getCountry());
        return dto;
    }
} 