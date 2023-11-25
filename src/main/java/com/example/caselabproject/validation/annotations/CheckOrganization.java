package com.example.caselabproject.validation.annotations;

import com.example.caselabproject.services.EntityOrganizationService;
import com.example.caselabproject.validation.CheckOrganizationValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Аннотированный элемент должен быть прикреплен к той же организации, что и
 * пользователь, от лица которого совершается действие (аутентифицированный пользователь).
 * <p>
 * Поддерживаемые типы:
 * <ul>
 *     <li>{@code Long}</li>
 * </ul>
 * <p>
 * {@code null} элементы поддерживаются.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = {CheckOrganizationValidator.class})
public @interface CheckOrganization {

    /**
     * Класс сервиса, из которого можно получить информацию о том, к какой организации
     * относится запрашиваемая сущность.
     *
     * @see EntityOrganizationService
     */
    Class<? extends EntityOrganizationService> serviceClass();

    String message() default "Attempt to access the materials of another organization";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}