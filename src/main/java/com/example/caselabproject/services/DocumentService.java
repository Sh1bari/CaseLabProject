package com.example.caselabproject.services;

import com.example.caselabproject.models.DTOs.request.DocumentCreateRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentCreateResponseDto;

public interface DocumentService {
    DocumentCreateResponseDto createDocument(DocumentCreateRequestDto request);
}
