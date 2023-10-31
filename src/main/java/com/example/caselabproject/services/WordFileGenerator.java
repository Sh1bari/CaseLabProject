package com.example.caselabproject.services;

import jakarta.validation.constraints.Min;

public interface WordFileGenerator {
    byte[] generateWordFileForDocumentById(@Min(value = 1L, message = "id can't be less than 1") Long documentId);
}
