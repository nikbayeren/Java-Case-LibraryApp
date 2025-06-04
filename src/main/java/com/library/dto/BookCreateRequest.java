package com.library.dto;

import jakarta.validation.constraints.*;

public class BookCreateRequest {
    @NotBlank(message = "Kitap başlığı boş olamaz")
    private String title;

    @NotNull(message = "Fiyat boş olamaz")
    @Positive(message = "Fiyat pozitif olmalıdır")
    @DecimalMin(value = "0.01", message = "Fiyat en az 0.01 olmalıdır")
    private Double price;

    @NotBlank(message = "ISBN13 boş olamaz")
    private String isbn13;

    @NotNull(message = "Yayın yılı boş olamaz")
    private Integer publishYear;

    @NotBlank(message = "Yayınevi adı boş olamaz")
    private String publisherName;

    @NotBlank(message = "Yazar adı soyadı boş olamaz")
    private String authorNameSurname;

    // Getter ve Setterlar
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public String getIsbn13() { return isbn13; }
    public void setIsbn13(String isbn13) { this.isbn13 = isbn13; }
    public Integer getPublishYear() { return publishYear; }
    public void setPublishYear(Integer publishYear) { this.publishYear = publishYear; }
    public String getPublisherName() { return publisherName; }
    public void setPublisherName(String publisherName) { this.publisherName = publisherName; }
    public String getAuthorNameSurname() { return authorNameSurname; }
    public void setAuthorNameSurname(String authorNameSurname) { this.authorNameSurname = authorNameSurname; }
} 