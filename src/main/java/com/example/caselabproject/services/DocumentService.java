package com.example.caselabproject.services;

import com.example.caselabproject.models.DTOs.request.DocumentCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.DocumentUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentCreateResponseDto;
import com.example.caselabproject.models.DTOs.response.DocumentResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface DocumentService {

    DocumentCreateResponseDto createDocument(String username, @Valid DocumentCreateRequestDto request);

    DocumentResponseDto findDocument(@Min(value = 1L, message = "Id can't be less than 1") Long documentId);

    List<DocumentResponseDto> filteredDocument(Pageable pageable, String name);

    DocumentResponseDto updateDocument(String username,
                                       @Valid DocumentUpdateRequestDto request,
                                       @Min(value = 1L, message = "Id can't be less than 1") Long documentId);

    boolean deleteDocument(String username,
                           @Min(value = 1L, message = "Id can't be less than 1") Long documentId);
}
