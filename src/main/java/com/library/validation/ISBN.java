package com.library.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ISBNValidator.class)
@Documented
public @interface ISBN {
    String message() default "Geçersiz ISBN formatı";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
} 