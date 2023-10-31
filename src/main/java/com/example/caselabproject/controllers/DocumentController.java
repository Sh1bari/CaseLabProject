package com.example.caselabproject.controllers;

import com.example.caselabproject.models.DTOs.request.DocumentCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.DocumentUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentCreateResponseDto;
import com.example.caselabproject.models.DTOs.response.DocumentResponseDto;
import com.example.caselabproject.services.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/doc")
@SecurityRequirement(name = "bearerAuth")
public class DocumentController {

    private final DocumentService documentService;
    @Operation(summary = "Create new document")
    @PostMapping("/")
    public ResponseEntity<DocumentCreateResponseDto> createDocument(
            Principal principal,
            @RequestBody DocumentCreateRequestDto requestDto) {
        DocumentCreateResponseDto responseDto = documentService.createDocument(principal.getName(), requestDto);
        return ResponseEntity
                .created(URI.create("/api/doc/" + responseDto.getId()))
                .body(responseDto);
    }
    //TODO добавить в дто creator_name, List<FileDto>, *documentConstructorType_name*
    @Operation(summary = "Get document by id")
    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponseDto> findDocument(@PathVariable Long id) {
        DocumentResponseDto responseDto = documentService.findDocument(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<DocumentResponseDto>> filteredSearch(
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "offset", required = false, defaultValue = "20") Integer offset,
            @RequestParam(name = "name", required = false, defaultValue = "") String name) {
        List<DocumentResponseDto> response = documentService.filteredDocument(PageRequest.of(page,offset), name);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentResponseDto> updateDocument(
            Principal principal,
            @RequestBody DocumentUpdateRequestDto requestDto,
            @PathVariable Long id) {
        DocumentResponseDto responseDto = documentService.updateDocument(principal.getName(), requestDto, id);
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
