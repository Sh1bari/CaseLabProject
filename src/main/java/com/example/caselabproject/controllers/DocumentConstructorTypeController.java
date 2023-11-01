package com.example.caselabproject.controllers;

import com.example.caselabproject.models.DTOs.request.DocumentConstructorTypePatchRequestDto;
import com.example.caselabproject.models.DTOs.request.DocumentConstructorTypeRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentConstructorTypeByIdResponseDto;
import com.example.caselabproject.models.DTOs.response.DocumentConstructorTypeCreateResponseDto;
import com.example.caselabproject.models.DTOs.response.DocumentConstructorTypeRecoverResponseDto;
import com.example.caselabproject.models.DTOs.response.DocumentConstructorTypeUpdateResponseDto;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.services.DocumentConstructorTypeService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Create new document type")
    @PostMapping("/")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<DocumentConstructorTypeCreateResponseDto> createDocumentType(
            @RequestBody @Valid DocumentConstructorTypeRequestDto documentTypeRequestDto) {
        DocumentConstructorTypeCreateResponseDto responseDto = typeService.create(documentTypeRequestDto);
        return ResponseEntity
                .created(URI.create("/api/doctype/" + responseDto.getId()))
                .body(responseDto);
    }

    //TODO Изменение fields (проверка имеется ли хотя бы 1 document у documentType)
    @PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<DocumentConstructorTypeUpdateResponseDto> renameDocumentType(
            @PathVariable @Min(value = 1L, message = "Id can't be less than 1") Long id,
            @RequestBody @Valid DocumentConstructorTypePatchRequestDto request) {
        DocumentConstructorTypeUpdateResponseDto response = typeService.updateById(id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
    @Operation(summary = "Delete document type by id")
    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> deleteDocumentType(
            @PathVariable @Min(value = 1L, message = "Id can't be less than 1") Long id) {
        typeService.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
    @Operation(summary = "Recover document type by id")
    @PostMapping("/{id}/recover")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<DocumentConstructorTypeRecoverResponseDto> recoverDocumentType(
            @PathVariable @Min(value = 1L, message = "Id can't be less than 1") Long id) {
        DocumentConstructorTypeRecoverResponseDto res = typeService.recoverById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }

    @Operation(summary = "Get document type by id")
    @GetMapping("/{id}")
    public ResponseEntity<DocumentConstructorTypeByIdResponseDto> getDocumentType(
            @PathVariable @Min(value = 1L, message = "Id can't be less than 1") Long id) {
        DocumentConstructorTypeByIdResponseDto response = typeService.getById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
    @Operation(summary = "Get document types by filters")
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
