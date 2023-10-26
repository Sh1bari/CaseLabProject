package com.example.caselabproject.controllers;

import com.example.caselabproject.models.DTOs.request.DocumentConstructorTypePatchRequestDto;
import com.example.caselabproject.models.DTOs.request.DocumentConstructorTypeRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentConstructorTypeResponseDto;
import com.example.caselabproject.services.DocumentConstructorTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@CrossOrigin
@RequestMapping("/doctype")
@RequiredArgsConstructor
public class DocumentConstructorTypeController {
    private final DocumentConstructorTypeService typeService;

    @PostMapping("/")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<DocumentConstructorTypeResponseDto> createDocumentType(
            @RequestBody DocumentConstructorTypeRequestDto documentTypeRequestDto) {
        DocumentConstructorTypeResponseDto responseDto = typeService.create(documentTypeRequestDto);
        return ResponseEntity
                .created(URI.create("/api/doctype/" + responseDto.getId()))
                .body(responseDto);
    }

    @PatchMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<DocumentConstructorTypeResponseDto> renameDocumentType(
            @PathVariable Long id,
            @RequestBody DocumentConstructorTypePatchRequestDto request) {
        DocumentConstructorTypeResponseDto response = typeService.updateById(id, request);

        return ResponseEntity
                .ok()
                .body(response);
    }
}
