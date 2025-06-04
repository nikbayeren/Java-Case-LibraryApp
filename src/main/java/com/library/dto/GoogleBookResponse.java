package com.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoogleBookResponse {
    private String title;
    private Double price;
    private String isbn13;
    private String publisherName;
    private String authorNameSurname;

    // Getter ve Setterlar
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public String getIsbn13() { return isbn13; }
    public void setIsbn13(String isbn13) { this.isbn13 = isbn13; }
    public String getPublisherName() { return publisherName; }
    public void setPublisherName(String publisherName) { this.publisherName = publisherName; }
    public String getAuthorNameSurname() { return authorNameSurname; }
    public void setAuthorNameSurname(String authorNameSurname) { this.authorNameSurname = authorNameSurname; }
} 