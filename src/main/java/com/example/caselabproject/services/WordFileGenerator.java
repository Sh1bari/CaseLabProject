package com.example.caselabproject.services;

import javax.validation.constraints.Min;

public interface WordFileGenerator {
    byte[] generateWordFileForDocumentById(@Min(1L) Long documentId);
}
