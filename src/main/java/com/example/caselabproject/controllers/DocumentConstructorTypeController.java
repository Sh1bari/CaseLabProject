package com.example.caselabproject.controllers;

import com.example.caselabproject.dtos.DocumentConstructorTypeDto;
import com.example.caselabproject.dtos.request.DocumentConstructorTypeRequestDto;
import com.example.caselabproject.dtos.response.DocumentConstructorTypeResponseDto;
import com.example.caselabproject.services.DocumentConstructorTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/doctype")
public class DocumentConstructorTypeController {
    private final DocumentConstructorTypeService typeService;

    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<DocumentConstructorTypeResponseDto> createDocumentType(
            @RequestBody DocumentConstructorTypeRequestDto documentTypeRequestDto) {
        DocumentConstructorTypeResponseDto responseDto = typeService.create(documentTypeRequestDto);
        return ResponseEntity
                .created(URI.create("/api/doctype/" + responseDto.getId()))
                .body(responseDto);
    }

    @PostMapping("/get?id={id}") // or is it better to use @GetMapping?
    public ResponseEntity<DocumentConstructorTypeDto> getDocumentTypeById(@PathVariable Long id) {
        Optional<DocumentConstructorTypeDto> documentConstructorType = typeService.findById(id);

        if (documentConstructorType.isPresent()) {
            return ResponseEntity
                    .ok(documentConstructorType.get());
        } else {
            return ResponseEntity
                    .notFound()
                    .build();
        }
    }
}
