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

import javax.validation.constraints.Min;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/doc")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping("/")
    public ResponseEntity<DocumentCreateResponseDto> create(
            @RequestBody DocumentCreateRequestDto requestDto) {
        DocumentCreateResponseDto responseDto = documentService.createDocument(requestDto);
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
    public ResponseEntity<List<Document>> filteredSearch(@RequestParam(name = "page") @Min(0) Integer page) {
        return ResponseEntity.ok(documentService.filteredDocument(page));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentUpdateResponseDto> update(
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
