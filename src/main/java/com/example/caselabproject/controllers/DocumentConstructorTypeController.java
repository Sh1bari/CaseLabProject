package com.example.caselabproject.controllers;

import com.example.caselabproject.dtos.request.DocumentConstructorTypeRequestDto;
import com.example.caselabproject.dtos.response.DocumentConstructorTypeResponseDto;
import com.example.caselabproject.services.DocumentConstructorTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/doctype")
public class DocumentConstructorTypeController {
    private final DocumentConstructorTypeService typeService;

    @PostMapping
    public ResponseEntity<DocumentConstructorTypeResponseDto> createDocumentType(
            DocumentConstructorTypeRequestDto documentTypeRequestDto) {
        DocumentConstructorTypeResponseDto responseDto = typeService.create(documentTypeRequestDto);
        return ResponseEntity
                .created(URI.create("/api/doctype/" + responseDto.getId()))
                .body(responseDto);
    }
}
