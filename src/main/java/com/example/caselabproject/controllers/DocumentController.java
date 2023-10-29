package com.example.caselabproject.controllers;

import com.example.caselabproject.models.DTOs.request.DocumentCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.DocumentUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentCreateResponseDto;
import com.example.caselabproject.models.DTOs.response.DocumentFindResponseDto;
import com.example.caselabproject.models.DTOs.response.DocumentUpdateResponseDto;
import com.example.caselabproject.models.entities.Document;
import com.example.caselabproject.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/doc")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping("/")
    public ResponseEntity<DocumentCreateResponseDto> create(
            Principal principal,
            @RequestBody DocumentCreateRequestDto requestDto) {
        DocumentCreateResponseDto responseDto = documentService.createDocument(principal.getName(), requestDto);
        return ResponseEntity
                .created(URI.create("/api/doc/" + responseDto.getId()))
                .body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentFindResponseDto> find(@PathVariable Long id) {
        DocumentFindResponseDto responseDto = documentService.findDocument(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<DocumentCreateResponseDto>> filteredSearch(
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "name", required = false, defaultValue = "") String name) {
        List<DocumentCreateResponseDto> response = documentService.filteredDocument(page, name);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentUpdateResponseDto> update(
            Principal principal,
            @RequestBody DocumentUpdateRequestDto requestDto,
            @PathVariable Long id) {
        DocumentUpdateResponseDto responseDto = documentService.updateDocument(principal.getName(), requestDto, id);
        return ResponseEntity
                .created(URI.create("/api/doc/" + responseDto.getId()))
                .body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(Principal principal, @PathVariable Long id) {
        documentService.deleteDocument(principal.getName(), id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
