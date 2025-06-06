package com.library.repository;

import com.library.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    
    Optional<Publisher> findByName(String name);

    List<Publisher> findFirst2ByOrderByIdAsc();
} 