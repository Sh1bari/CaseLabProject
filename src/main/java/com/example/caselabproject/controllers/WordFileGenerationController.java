package com.example.caselabproject.controllers;

import com.example.caselabproject.exceptions.AppError;
import com.example.caselabproject.services.WordFileGenerator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.util.InMemoryResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class WordFileGenerationController {
    private final WordFileGenerator wordFileGenerator;

    @Operation(summary = "Generates a word file for a document with the specified id and gives it as a file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Word file was successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid path variable",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))}),
            @ApiResponse(responseCode = "401", description = "User is not authenticated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))}),
            @ApiResponse(responseCode = "404", description = "Document with provided id isn't found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))}),
            @ApiResponse(responseCode = "500", description = "Could not create word file for document",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))})
    })
    @GetMapping("/doc/{id}/generate")
    public ResponseEntity<Resource> generateAndDownloadFile(
            @PathVariable("id") @Min(value = 1L, message = "Id can't be less than 1") Long id) {
        byte[] wordFile = wordFileGenerator.generateWordFileForDocumentById(id);

        Resource resource = new InMemoryResource(wordFile);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, String.valueOf(ContentDisposition.attachment().filename("generated-document.docx").build()))
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(wordFile.length)
                .body(resource);
    }
}
