package com.example.caselabproject.controllers;

import com.example.caselabproject.models.DTOs.request.DocumentCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.DocumentUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentCreateResponseDto;
import com.example.caselabproject.models.DTOs.response.DocumentFindResponseDto;
import com.example.caselabproject.models.DTOs.response.DocumentUpdateResponseDto;
import com.example.caselabproject.models.entities.Document;
import com.example.caselabproject.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/doc")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping("/")
    ResponseEntity<DocumentCreateResponseDto> create(
            @RequestBody DocumentCreateRequestDto requestDto) {
        DocumentCreateResponseDto responseDto = documentService.createDocument(requestDto);
        return ResponseEntity
                .created(URI.create("/api/doc/" + responseDto.getId()))
                .body(responseDto);
    }

    @GetMapping("/{id}")
    ResponseEntity<DocumentFindResponseDto> find(@PathVariable Long id) {
        DocumentFindResponseDto responseDto = documentService.findDocument(id);
        return ResponseEntity
                .created(URI.create("/api/doc/" + responseDto.getId()))
                .body(responseDto);
    }

    @GetMapping("/filter")
    List<Document> filteredSearch(@RequestParam int limit, @RequestParam int offset) {
        return documentService.filteredDocument(limit, offset);
    }

    @PutMapping("/{id}")
    ResponseEntity<DocumentUpdateResponseDto> update(
            @RequestBody DocumentUpdateRequestDto requestDto, @PathVariable Long id) {
        Document document = requestDto.mapToEntity();
        document.setId(id);
        DocumentUpdateResponseDto responseDto = documentService.updateDocument(document);
        return ResponseEntity
                .created(URI.create("/api/doc/" + responseDto.getId()))
                .body(responseDto);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        documentService.deleteDocument(id);
    }
}
