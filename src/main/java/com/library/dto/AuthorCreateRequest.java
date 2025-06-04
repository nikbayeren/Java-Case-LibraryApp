package com.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AuthorCreateRequest {
    @NotBlank(message = "Yazar adı soyadı boş olamaz")
    private String name;
    @NotNull(message = "Doğum yılı boş olamaz")
    private Integer birthYear;
    @NotBlank(message = "Uyruk boş olamaz")
    private String country;
    private String biography;

    public String getName() {
        return name;
    }
    public Integer getBirthYear() {
        return birthYear;
    }
    public String getCountry() {
        return country;
    }
    public String getBiography() {
        return biography;
    }
} 