package com.library.dto;

public class AuthorResponseDTO {
    private Long id;
    private String name;
    private Integer birthYear;
    private String country;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getBirthYear() { return birthYear; }
    public void setBirthYear(Integer birthYear) { this.birthYear = birthYear; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
} 