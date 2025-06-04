package com.library.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;

@FeignClient(name = "google-books", url = "https://www.googleapis.com/books/v1")
public interface GoogleBooksClient {
    @GetMapping("/volumes")
    Map<String, Object> searchBooks(@RequestParam("q") String query);
} 