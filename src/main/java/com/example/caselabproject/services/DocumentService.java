package com.example.caselabproject.services;

import com.example.caselabproject.models.DTOs.request.DocumentCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.DocumentUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentCreateResponseDto;
import com.example.caselabproject.models.DTOs.response.DocumentFindResponseDto;
import com.example.caselabproject.models.DTOs.response.DocumentUpdateResponseDto;
import com.example.caselabproject.models.entities.Document;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

public interface DocumentService {

    DocumentCreateResponseDto createDocument(String username, @Valid DocumentCreateRequestDto request);

    DocumentFindResponseDto findDocument(@Min(1L) Long documentId);

    List<DocumentCreateResponseDto> filteredDocument(@Min(0) Integer page, String name);

    DocumentUpdateResponseDto updateDocument(String username, DocumentUpdateRequestDto request, Long documentId);

    void deleteDocument(String username, @Min(1L) Long documentId);
}
