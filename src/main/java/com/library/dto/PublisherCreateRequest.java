package com.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublisherCreateRequest {
    
    @NotBlank(message = "Yayınevi adı boş olamaz")
    private String name;
    
    @NotNull(message = "Kuruluş yılı boş olamaz")
    private Integer establishmentYear;
    
    @NotBlank(message = "Adres boş olamaz")
    private String address;
} 