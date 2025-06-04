package com.library.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.library.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    List<Book> findByAuthorId(Long authorId);
    List<Book> findByPublisherId(Long publisherId);
    List<Book> findByPublisherIdIn(List<Long> publisherIds);
    List<Book> findByTitleStartingWithIgnoreCase(String prefix);
    List<Book> findByPublishYearGreaterThan(Integer year);
    List<Book> findByPriceBetween(Double minPrice, Double maxPrice);
    
    Page<Book> findAll(Pageable pageable);
    
    @Query("SELECT b FROM Book b WHERE " +
           "LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(b.isbn13) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Book> fullTextSearch(String query);
} 