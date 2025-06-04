package com.library.service;

import com.library.dto.PublisherDTO;
import com.library.dto.PublisherResponseDTO;
import com.library.entity.Publisher;
import com.library.exception.PublisherNotFoundException;
import com.library.repository.PublisherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublisherService {

    private final PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    @Transactional
    public PublisherResponseDTO createPublisher(PublisherDTO publisherDTO) {
        Publisher publisher = new Publisher();
        publisher.setName(publisherDTO.getName());
        publisher.setEstablishmentYear(publisherDTO.getEstablishmentYear());
        publisher.setAddress(publisherDTO.getAddress());
        publisher.setPhone(publisherDTO.getPhone());
        publisher.setEmail(publisherDTO.getEmail());

        Publisher savedPublisher = publisherRepository.save(publisher);
        return convertToResponseDTO(savedPublisher);
    }

    @Transactional(readOnly = true)
    public List<PublisherResponseDTO> getAllPublishers() {
        return publisherRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PublisherResponseDTO getPublisherById(Long id) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new PublisherNotFoundException("Publisher not found with id: " + id));
        return convertToResponseDTO(publisher);
    }

    @Transactional
    public PublisherResponseDTO updatePublisher(Long id, PublisherDTO publisherDTO) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new PublisherNotFoundException("Publisher not found with id: " + id));

        publisher.setName(publisherDTO.getName());
        publisher.setEstablishmentYear(publisherDTO.getEstablishmentYear());
        publisher.setAddress(publisherDTO.getAddress());
        publisher.setPhone(publisherDTO.getPhone());
        publisher.setEmail(publisherDTO.getEmail());

        Publisher updatedPublisher = publisherRepository.save(publisher);
        return convertToResponseDTO(updatedPublisher);
    }

    @Transactional
    public void deletePublisher(Long id) {
        if (!publisherRepository.existsById(id)) {
            throw new PublisherNotFoundException("Publisher not found with id: " + id);
        }
        publisherRepository.deleteById(id);
    }

    public List<PublisherResponseDTO> getFirst2PublishersWithBooksAndAuthors() {
        return publisherRepository.findFirst2ByOrderByIdAsc().stream()
            .map(this::convertToResponseDTOWithBooksAndAuthors)
            .collect(Collectors.toList());
    }

    private PublisherResponseDTO convertToResponseDTO(Publisher publisher) {
        PublisherResponseDTO dto = new PublisherResponseDTO();
        dto.setId(publisher.getId());
        dto.setName(publisher.getName());
        dto.setEstablishmentYear(publisher.getEstablishmentYear());
        dto.setAddress(publisher.getAddress());
        dto.setPhone(publisher.getPhone());
        dto.setEmail(publisher.getEmail());
        return dto;
    }

    private PublisherResponseDTO convertToResponseDTOWithBooksAndAuthors(Publisher publisher) {
        PublisherResponseDTO dto = convertToResponseDTO(publisher);
        // Kitap ve yazar bilgilerini response'a eklemek için genişletilebilir
        // Şu an PublisherResponseDTO'da kitap/yazar listesi yok, istenirse eklenebilir
        return dto;
    }
} 