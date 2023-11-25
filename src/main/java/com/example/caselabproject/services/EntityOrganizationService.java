package com.example.caselabproject.services;

import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;

@Validated
public interface EntityOrganizationService {
    Long getOrganizationIdByEntityId(@Min(value = 1L, message = "EntityId must be >= 1") Long entityId);
}
