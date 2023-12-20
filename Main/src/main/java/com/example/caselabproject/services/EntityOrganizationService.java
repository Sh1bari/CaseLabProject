package com.example.caselabproject.services;

import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;

@Validated
public interface EntityOrganizationService {
    /**
     * Метод ищет id организации, к которой прикреплена сущность с entityId.
     *
     * @param entityId идентификатор сущности.
     * @return id организации, к которой прикреплена сущность с entityId.
     */
    Long getOrganizationIdByEntityId(@Min(value = 1L, message = "EntityId must be more than 0") Long entityId);
}
