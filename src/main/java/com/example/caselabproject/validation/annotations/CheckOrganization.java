package com.example.caselabproject.validation.annotations;

import com.example.caselabproject.services.EntityOrganizationService;
import com.example.caselabproject.validation.CheckOrganizationValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Аннотированный элемент должен быть прикреплен к той же организации, что и
 * пользователь, от лица которого совершается действие (аутентифицированного пользователя).
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = {CheckOrganizationValidator.class})
public @interface CheckOrganization {

    Class<? extends EntityOrganizationService> serviceClass();

    String message() default "Attempt to access the materials of another organization";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
