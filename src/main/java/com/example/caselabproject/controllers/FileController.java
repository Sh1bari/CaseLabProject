package com.example.caselabproject.controllers;

import com.example.caselabproject.exceptions.AppError;
import com.example.caselabproject.models.DTOs.response.FileDownloadResponseDto;
import com.example.caselabproject.models.DTOs.response.FileResponseDto;
import com.example.caselabproject.services.DocumentService;
import com.example.caselabproject.services.FileService;
import com.example.caselabproject.validation.annotations.CheckOrganization;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("doc/{docId}/file")
@SecurityRequirement(name = "bearerAuth")
public class FileController {

    private final FileService fileService;

    @Operation(summary = "Add new file", description = "Add new file to document by document's Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "File added",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = FileResponseDto.class))
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
    @PostMapping("/")
    public ResponseEntity<List<FileResponseDto>> addFileToDocument(
            @RequestParam("file") MultipartFile file,
            Principal principal,
            @CheckOrganization(serviceClass = DocumentService.class)
            @PathVariable @Min(value = 1L, message = "Id can't be less than 1") Long docId) {
        List<FileResponseDto> response = fileService.addFile(principal.getName(), file, docId);
        return ResponseEntity
                .created(URI.create("/api/doc/" + docId + "/file/"))
                .body(response);
    }

    @Operation(summary = "Get file by document's id", description = "Retrieves list of files by document's ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of files retrieved successfully",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = FileResponseDto.class))
                    }),
            @ApiResponse(responseCode = "204", description = "No files found"),
            @ApiResponse(responseCode = "404", description = "Document not found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppError.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppError.class))
                    })
    })
    @GetMapping("/")
    public ResponseEntity<List<FileResponseDto>> getFilesByDocumentId(
            @CheckOrganization(serviceClass = DocumentService.class)
            @PathVariable @Min(value = 1L, message = "Id can't be less than 1") Long docId,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "offset", required = false, defaultValue = "20") Integer offset) {
        List<FileResponseDto> response = fileService.getFiles(docId, PageRequest.of(page, offset));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Operation(summary = "Download file by file's id", description = "Download file by document's ID and file's ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Files downloaded successfully",
                    content = {
                            @Content(mediaType = "application/json")
                    }),
            @ApiResponse(responseCode = "404", description = "Document or file not found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppError.class))
                    })
    })
    @GetMapping("/{fileId}/download")
    public ResponseEntity<?> downloadFile(
            @CheckOrganization(serviceClass = DocumentService.class)
            @PathVariable @Min(value = 1L, message = "Id can't be less than 1") Long docId,
            @PathVariable @Min(value = 1L, message = "Id can't be less than 1") Long fileId) throws IOException {
        FileDownloadResponseDto response = fileService.downloadFile(docId, fileId);
        byte[] file = response.getBytes();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.valueOf(response.getType()))
                .contentLength(file.length)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(response.getName()).toString())
                .body(file);
    }

    @Operation(summary = "Update file by file's id", description = "Update file by document's ID and file's ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File updated successfully",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = FileResponseDto.class))
                    }),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Document or file not found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppError.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppError.class))
                    })
    })
    @PutMapping("/{fileId}")
    public ResponseEntity<List<FileResponseDto>> updateFileByDocumentId(
            @RequestParam("file") MultipartFile file,
            Principal principal,
            @CheckOrganization(serviceClass = DocumentService.class)
            @PathVariable @Min(value = 1L, message = "Id can't be less than 1") Long docId,
            @PathVariable @Min(value = 1L, message = "Id can't be less than 1") Long fileId) {
        List<FileResponseDto> response = fileService.updateFile(principal.getName(), file, docId, fileId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Operation(summary = "Delete file by file's id", description = "Delete file by document's ID and file's ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "File deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Document or file not found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppError.class))
                    })
    })
    @DeleteMapping("/{fileId}")
    public ResponseEntity<?> deleteFile(
            Principal principal,
            @CheckOrganization(serviceClass = DocumentService.class)
            @PathVariable @Min(value = 1L, message = "Id can't be less than 1") Long docId,
            @PathVariable @Min(value = 1L, message = "Id can't be less than 1") Long fileId) {
        fileService.deleteFile(principal.getName(), docId, fileId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
