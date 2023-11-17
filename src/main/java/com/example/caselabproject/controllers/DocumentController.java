package com.example.caselabproject.controllers;

import com.example.caselabproject.exceptions.AppError;
import com.example.caselabproject.models.DTOs.request.document.DocumentRequestDto;
import com.example.caselabproject.models.DTOs.response.document.DocumentCreateResponseDto;
import com.example.caselabproject.models.DTOs.response.document.DocumentResponseDto;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.services.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Document created",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DocumentCreateResponseDto.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppError.class))
                    })
    })
    @PostMapping("/")
    public ResponseEntity<DocumentCreateResponseDto> createDocument(
            Principal principal,
            @RequestBody @Valid DocumentRequestDto requestDto) {
        DocumentCreateResponseDto responseDto = documentService.createDocument(principal.getName(), requestDto);
        return ResponseEntity
                .created(URI.create("/api/doc/" + responseDto.getId()))
                .body(responseDto);
    }

    @Operation(summary = "Get document by id", description = "Retrieves document by his ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document found and returned successfully",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DocumentResponseDto.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Document not found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppError.class))
                    })
    })
    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponseDto> findDocument(
            @PathVariable @Min(value = 1L, message = "Id can't be less than 1") Long id) {
        DocumentResponseDto responseDto = documentService.findDocument(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

    @Operation(summary = "Get list of documents", description = "Retrieves a list of documents with pagination and optional filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of documents retrieved successfully",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DocumentResponseDto.class))
                    }),
            @ApiResponse(responseCode = "204", description = "No documents found"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppError.class))
                    })
    })
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

    @Operation(summary = "Update document by id", description = "Update document by his ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document updated successfully",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DocumentResponseDto.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppError.class))
                    }),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Document not found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppError.class))
                    })
    })
    @PutMapping("/{id}")
    public ResponseEntity<DocumentResponseDto> updateDocument(
            Principal principal,
            @RequestBody @Valid DocumentRequestDto requestDto,
            @PathVariable @Min(value = 1L, message = "Id can't be less than 1") Long id) {
        DocumentResponseDto responseDto = documentService.updateDocument(principal.getName(), requestDto, id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

    @Operation(summary = "Delete document by id", description = "Delete document by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Document deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Document not found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppError.class))
                    })
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDocument(
            Principal principal,
            @PathVariable @Min(value = 1L, message = "Id can't be less than 1") Long id) {
        documentService.deleteDocument(principal.getName(), id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
