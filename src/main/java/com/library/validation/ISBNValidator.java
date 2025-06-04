package com.library.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ISBNValidator implements ConstraintValidator<ISBN, String> {
    
    @Override
    public void initialize(ISBN constraintAnnotation) {
    }
    
    @Override
    public boolean isValid(String isbn, ConstraintValidatorContext context) {
        if (isbn == null || isbn.trim().isEmpty()) {
            return false;
        }
        
        // Sadece rakamları al
        isbn = isbn.replaceAll("[^0-9]", "");
        
        // ISBN-13 formatı kontrolü
        if (isbn.length() != 13) {
            return false;
        }
        
        try {
            int sum = 0;
            for (int i = 0; i < 12; i++) {
                int digit = Integer.parseInt(isbn.substring(i, i + 1));
                sum += (i % 2 == 0) ? digit : digit * 3;
            }
            
            int checksum = 10 - (sum % 10);
            if (checksum == 10) {
                checksum = 0;
            }
            
            return checksum == Integer.parseInt(isbn.substring(12));
        } catch (NumberFormatException e) {
            return false;
        }
    }
} 