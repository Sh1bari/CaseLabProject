package com.example.caselabproject.services;

import com.example.caselabproject.validation.annotations.CheckOrganization;
import jakarta.validation.constraints.Min;

public interface WordFileGenerator {
    byte[] generateWordFileForDocumentById(
            @CheckOrganization(serviceClass = DocumentService.class)
            @Min(value = 1L, message = "id can't be less than 1") Long documentId);
}
