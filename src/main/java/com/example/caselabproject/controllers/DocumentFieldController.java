package com.example.caselabproject.controllers;

import com.example.caselabproject.models.DTOs.request.DocumentFieldUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentResponseDto;
import com.example.caselabproject.services.DocumentFieldService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/doc/{documentId}/fields")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "bearerAuth")
public class DocumentFieldController {

    private final DocumentFieldService documentFieldService;

    @PatchMapping
    public ResponseEntity<DocumentResponseDto> replaceDocumentFields(
            @PathVariable("documentId") @Min(value = 1L, message = "Id must be >= 1") Long documentId,
            @RequestBody
            @UniqueElements(message = "Fields must be unique")
            @Size(min = 1, message = "Request doesn't contain fields")
            List<@Valid DocumentFieldUpdateRequestDto> documentFieldDtos) {
        return ResponseEntity
                .ok(documentFieldService.replaceDocumentFields(documentId, documentFieldDtos));
    }
}
