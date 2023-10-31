package com.example.caselabproject.controllers;

import com.example.caselabproject.models.DTOs.request.FileCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.FileUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.FileResponseDto;
import com.example.caselabproject.services.FileService;
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
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("doc/{docId}/file")
@SecurityRequirement(name = "bearerAuth")
public class FileController {

    private final FileService fileService;

    @PostMapping("/")
    public ResponseEntity<List<FileResponseDto>> addFileToDocument(
            Principal principal, @RequestBody @Valid FileCreateRequestDto request,
            @PathVariable @Min(value = 1L, message = "Id can't be less than 1") Long docId) {
        List<FileResponseDto> response = fileService.addFile(principal.getName(), request, docId);
        return ResponseEntity
                .created(URI.create("/api/doc/" + docId + "/file/"))
                .body(response);
    }

    @GetMapping("/")
    public ResponseEntity<List<FileResponseDto>> getFileByDocumentId(
            @PathVariable @Min(value = 1L, message = "Id can't be less than 1") Long docId,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "offset", required = false, defaultValue = "20") Integer offset) {
        List<FileResponseDto> response = fileService.getFiles(docId, PageRequest.of(page, offset));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping("/{fileId}")
    public ResponseEntity<List<FileResponseDto>> updateFileByDocumentId(
            Principal principal, @RequestBody @Valid FileUpdateRequestDto request,
            @PathVariable @Min(value = 1L, message = "Id can't be less than 1") Long docId,
            @PathVariable @Min(value = 1L, message = "Id can't be less than 1") Long fileId) {
        List<FileResponseDto> response = fileService.updateFile(principal.getName(), request, docId, fileId);
        return ResponseEntity
                .created(URI.create("/api/doc/" + docId + "/file/"))
                .body(response);
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<List<FileResponseDto>> delete(
            Principal principal,
            @PathVariable @Min(value = 1L, message = "Id can't be less than 1") Long docId,
            @PathVariable @Min(value = 1L, message = "Id can't be less than 1") Long fileId) {
        List<FileResponseDto> response = fileService.deleteFile(principal.getName(), docId, fileId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
