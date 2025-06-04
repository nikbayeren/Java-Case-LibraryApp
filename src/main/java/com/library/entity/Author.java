package com.library.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "author")
public class Author implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "birth_year")
    private Integer birthYear;

    @Column(name = "country")
    private String country;

    @Column(name = "biography", length = 1000, nullable = true)
    private String biography;

    @JsonManagedReference
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Book> books = new ArrayList<>();

    public Author() {}
    public Author(Long id, String name, Integer birthYear, String country, String biography, List<Book> books) {
        this.id = id;
        this.name = name;
        this.birthYear = birthYear;
        this.country = country;
        this.biography = biography;
        this.books = books;
    }
    public static AuthorBuilder builder() { return new AuthorBuilder(); }
    public static class AuthorBuilder {
        private Long id;
        private String name;
        private Integer birthYear;
        private String country;
        private String biography;
        private List<Book> books = new ArrayList<>();
        public AuthorBuilder id(Long id) { this.id = id; return this; }
        public AuthorBuilder name(String name) { this.name = name; return this; }
        public AuthorBuilder birthYear(Integer birthYear) { this.birthYear = birthYear; return this; }
        public AuthorBuilder country(String country) { this.country = country; return this; }
        public AuthorBuilder biography(String biography) { this.biography = biography; return this; }
        public AuthorBuilder books(List<Book> books) { this.books = books; return this; }
        public Author build() { return new Author(id, name, birthYear, country, biography, books); }
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getBirthYear() { return birthYear; }
    public void setBirthYear(Integer birthYear) { this.birthYear = birthYear; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getBiography() { return biography; }
    public void setBiography(String biography) { this.biography = biography; }
    public List<Book> getBooks() { return books; }
    public void setBooks(List<Book> books) { this.books = books; }
} 