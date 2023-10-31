package com.example.caselabproject.controllers;

import com.example.caselabproject.models.DTOs.request.DocumentConstructorTypePatchRequestDto;
import com.example.caselabproject.models.DTOs.request.DocumentConstructorTypeRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentConstructorTypeResponseDto;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.services.DocumentConstructorTypeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/doctype")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class DocumentConstructorTypeController {

    private final DocumentConstructorTypeService typeService;

    @PostMapping("/")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<DocumentConstructorTypeResponseDto> createDocumentType(
            @RequestBody DocumentConstructorTypeRequestDto documentTypeRequestDto) {

        DocumentConstructorTypeResponseDto responseDto = typeService.create(documentTypeRequestDto);
        return ResponseEntity
                .created(URI.create("/api/doctype/" + responseDto.getId()))
                .body(responseDto);
    }

    /**
     * Важно добавить изменение fields
     * @author
     */
    @PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<DocumentConstructorTypeResponseDto> renameDocumentType(
            @PathVariable Long id,
            @RequestBody DocumentConstructorTypePatchRequestDto request) {
        DocumentConstructorTypeResponseDto response = typeService.updateById(id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> deleteDocumentType(@PathVariable Long id) {
        typeService.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentConstructorTypeResponseDto> getDocumentType(@PathVariable Long id) {
        DocumentConstructorTypeResponseDto response = typeService.getById(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<DocumentConstructorTypeResponseDto>> getAllDocumentTypes(
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            @RequestParam(name = "state", required = false, defaultValue = "ACTIVE") RecordState state,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {

        List<DocumentConstructorTypeResponseDto> response =
                typeService.getAllContaining(name, state, page, size);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
