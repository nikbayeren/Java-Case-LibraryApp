package com.library;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@OpenAPIDefinition(
    info = @Info(
        title = "Kütüphane Yönetim Sistemi API",
        version = "1.0",
        description = "Kütüphane yönetim sistemi için REST API dokümantasyonu"
    )
)
public class LibraryAppApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(LibraryAppApplication.class, args);
    }
} 