package com.example.caselabproject.services;

import javax.validation.constraints.Min;

public interface WordFileGenerator {
    byte[] generateWordFileForDocumentById(@Min(value = 1L, message = "id can't be less than 1") Long documentId);
}
