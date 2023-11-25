package com.example.caselabproject.multitenancy.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = {CheckOrganizationValidator.class})
public @interface CheckOrganization {

    String message() default "Attempt to access the materials of another organization";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
