package com.example.caselabproject.controllers;

import com.example.caselabproject.models.DTOs.request.DocumentConstructorTypeRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentConstructorTypeResponseDto;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.repositories.UserRepository;
import com.example.caselabproject.services.DocumentConstructorTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@CrossOrigin
@RequestMapping("/doctype")
public class DocumentConstructorTypeController {
    @Autowired
    private DocumentConstructorTypeService typeService;

    @PostMapping("/")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<DocumentConstructorTypeResponseDto> createDocumentType(
            @RequestBody DocumentConstructorTypeRequestDto documentTypeRequestDto){
        DocumentConstructorTypeResponseDto responseDto = typeService.create(documentTypeRequestDto);
        return ResponseEntity
                .created(URI.create("/api/doctype/" + responseDto.getId()))
                .body(responseDto);
    }
}
