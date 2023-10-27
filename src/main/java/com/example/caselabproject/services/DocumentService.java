package com.example.caselabproject.services;

import com.example.caselabproject.models.DTOs.request.DocumentCreateRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentCreateResponseDto;
import com.example.caselabproject.models.DTOs.response.DocumentFindResponseDto;
import com.example.caselabproject.models.DTOs.response.DocumentUpdateResponseDto;
import com.example.caselabproject.models.entities.Document;

import java.util.List;

public interface DocumentService {

    DocumentCreateResponseDto createDocument(DocumentCreateRequestDto request);

    DocumentFindResponseDto findDocument(Long documentId);

    DocumentUpdateResponseDto updateDocument(Document document);

    List<Document> filteredDocument(int limit, int offset);

    void deleteDocument(Long id);
}
