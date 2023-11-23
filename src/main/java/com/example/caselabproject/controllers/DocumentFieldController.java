package com.example.caselabproject.controllers;

import com.example.caselabproject.exceptions.AppError;
import com.example.caselabproject.models.DTOs.request.DocumentFieldUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentResponseDto;
import com.example.caselabproject.services.DocumentFieldService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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


    @Operation(summary = "Replaces the field values in the document. If any of the fields did not " +
            "exist in the document, it will be added. If any of the fields you add did not exist in " +
            "the database, it will be created.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The document fields have been successfully updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DocumentResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid value of path variable or request body",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))}),
            @ApiResponse(responseCode = "404", description = "Document not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))}),
    })
    @Secured("ROLE_USER")
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
