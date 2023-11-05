package com.example.caselabproject.controllers;

import com.example.caselabproject.models.DTOs.request.DocumentCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.DocumentUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentCreateResponseDto;
import com.example.caselabproject.models.DTOs.response.DocumentResponseDto;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.services.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.time.LocalDateTime;
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
            @RequestBody @Valid DocumentCreateRequestDto requestDto) {
        DocumentCreateResponseDto responseDto = documentService.createDocument(principal.getName(), requestDto);
        return ResponseEntity
                .created(URI.create("/api/doc/" + responseDto.getId()))
                .body(responseDto);
    }

    //TODO добавить в дто creator_name, List<FileDto>, *documentConstructorType_name*
    @Operation(summary = "Get document by id")
    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponseDto> findDocument(
            @PathVariable @Min(value = 1L, message = "Id can't be less than 1") Long id) {
        DocumentResponseDto responseDto = documentService.findDocument(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<DocumentResponseDto>> filteredSearch(
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "limit", required = false, defaultValue = "20") Integer limit,
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            @RequestParam(name = "state", required = false, defaultValue = "ACTIVE") RecordState recordState,
            @RequestParam(name = "creationDateStart", required = false, defaultValue = "2000-01-01T00:00:00") LocalDateTime startDate,
            @RequestParam(name = "creationDateEnd", required = false, defaultValue = "3000-01-01T00:00:00") LocalDateTime endDate) {
        List<DocumentResponseDto> response = documentService
                .filteredDocument(PageRequest.of(page, limit), name, recordState, startDate, endDate);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentResponseDto> updateDocument(
            Principal principal,
            @RequestBody @Valid DocumentUpdateRequestDto requestDto,
            @PathVariable @Min(value = 1L, message = "Id can't be less than 1") Long id) {
        DocumentResponseDto responseDto = documentService.updateDocument(principal.getName(), requestDto, id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDocument(Principal principal,
                                            @PathVariable @Min(value = 1L, message = "Id can't be less than 1") Long id) {
        documentService.deleteDocument(principal.getName(), id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
