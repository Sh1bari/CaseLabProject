package com.example.caselabproject.services;

import com.example.caselabproject.models.DTOs.request.DocumentCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.DocumentUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentResponseDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

public interface DocumentService {

    DocumentResponseDto createDocument(String username, @Valid DocumentCreateRequestDto request);

    DocumentResponseDto findDocument(@Min(1L) Long documentId);

    List<DocumentResponseDto> filteredDocument(@Min(0) Integer page, String name);

    DocumentResponseDto updateDocument(String username, DocumentUpdateRequestDto request, Long documentId);

    void deleteDocument(String username, @Min(1L) Long documentId);
}
