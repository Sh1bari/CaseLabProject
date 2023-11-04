package com.example.caselabproject.controllers;

import com.example.caselabproject.exceptions.AppError;
import com.example.caselabproject.models.DTOs.request.DocumentConstructorTypePatchRequestDto;
import com.example.caselabproject.models.DTOs.request.DocumentConstructorTypeRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentConstructorTypeByIdResponseDto;
import com.example.caselabproject.models.DTOs.response.DocumentConstructorTypeCreateResponseDto;
import com.example.caselabproject.models.DTOs.response.DocumentConstructorTypeRecoverResponseDto;
import com.example.caselabproject.models.DTOs.response.DocumentConstructorTypeUpdateResponseDto;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.services.DocumentConstructorTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/doctype")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "bearerAuth")
public class DocumentConstructorTypeController {
    private final DocumentConstructorTypeService typeService;

    @Operation(summary = "Create new document type, secured by admin")
    @ApiResponses(value = @ApiResponse(responseCode = "201", description = "Document type created",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = DocumentConstructorTypeCreateResponseDto.class))}))
    @PostMapping("/")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<DocumentConstructorTypeCreateResponseDto> createDocumentType(
            @RequestBody @Valid DocumentConstructorTypeRequestDto documentTypeRequestDto) {
        DocumentConstructorTypeCreateResponseDto responseDto = typeService.create(documentTypeRequestDto);
        return ResponseEntity
                .created(URI.create("/api/doctype/" + responseDto.getId()))
                .body(responseDto);
    }

    @Operation(summary = "Update an existing document type, secured by admin")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Document type changed",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = DocumentConstructorTypeUpdateResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "Document type with provided id isn't found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))}),
            @ApiResponse(responseCode = "409", description = "Document type already has associated documents",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))})})
    @PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<DocumentConstructorTypeUpdateResponseDto> updateDocumentType(
            @PathVariable @Min(value = 1L, message = "Id can't be less than 1") Long id,
            @RequestBody @Valid DocumentConstructorTypeRequestDto request) {
        DocumentConstructorTypeUpdateResponseDto response = typeService.update(id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Operation(summary = "Updates an existing document type by changing its name and prefix, secured by admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document type renamed",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DocumentConstructorTypeUpdateResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "Document type with provided id isn't found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))})})
    @PatchMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<DocumentConstructorTypeUpdateResponseDto> updateDocumentTypeNameAndPrefix(
            @PathVariable @Min(value = 1L, message = "Id can't be less than 1") Long id,
            @RequestBody @Valid DocumentConstructorTypePatchRequestDto request) {
        DocumentConstructorTypeUpdateResponseDto response = typeService.updateNameAndPrefix(id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Operation(summary = "Delete a document type, secured by admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Document type deleted",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Document type with provided id isn't found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))}),
            @ApiResponse(responseCode = "409", description = "Document type with provided id already deleted",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))})})
    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> deleteDocumentType(
            @PathVariable @Min(value = 1L, message = "Id can't be less than 1") Long id) {
        typeService.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Operation(summary = "Recover document type by id, secured by admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document type recovered",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DocumentConstructorTypeRecoverResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "Document type with provided id isn't found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))}),
            @ApiResponse(responseCode = "409", description = "Document type with provided id already active",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))})})
    @PostMapping("/{id}/recover")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<DocumentConstructorTypeRecoverResponseDto> recoverDocumentType(
            @PathVariable @Min(value = 1L, message = "Id can't be less than 1") Long id) {
        DocumentConstructorTypeRecoverResponseDto response = typeService.recoverById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Operation(summary = "Get document type by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document type found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DocumentConstructorTypeByIdResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "Document type with provided id isn't found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))})})
    @GetMapping("/{id}")
    public ResponseEntity<DocumentConstructorTypeByIdResponseDto> getDocumentType(
            @PathVariable @Min(value = 1L, message = "Id can't be less than 1") Long id) {
        DocumentConstructorTypeByIdResponseDto response = typeService.getById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Operation(summary = "Get document types by filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Page with document types found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class))}),
            @ApiResponse(responseCode = "404", description = "Page with document types not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))})})
    @GetMapping("/filtered")
    public ResponseEntity<List<DocumentConstructorTypeByIdResponseDto>> getAllDocumentTypes(
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            @RequestParam(name = "state", required = false, defaultValue = "ACTIVE") RecordState state,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        List<DocumentConstructorTypeByIdResponseDto> response =
                typeService.getAllContaining(name, state, page, size);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
