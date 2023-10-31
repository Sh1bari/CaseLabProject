package com.example.caselabproject.controllers;

import com.example.caselabproject.models.DTOs.request.FileCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.FileUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.FileResponseDto;
import com.example.caselabproject.services.FileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
            Principal principal, @RequestBody FileCreateRequestDto request, @PathVariable Long docId) {
        List<FileResponseDto> response = fileService.addFile(principal.getName(), request, docId);
        return ResponseEntity
                .created(URI.create("/api/doc/" + docId + "/file/"))
                .body(response);
    }

    @GetMapping("/")
    public ResponseEntity<List<FileResponseDto>> getFileByDocumentId(
            @PathVariable Long docId,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page) {
        List<FileResponseDto> response = fileService.getFiles(docId, page);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping("/{fileId}")
    public ResponseEntity<List<FileResponseDto>> updateFileByDocumentId(
            Principal principal, @RequestBody FileUpdateRequestDto request,
            @PathVariable Long docId, @PathVariable Long fileId) {
        List<FileResponseDto> response = fileService.updateFile(principal.getName(), request, docId, fileId);
        return ResponseEntity
                .created(URI.create("/api/doc/" + docId + "/file/"))
                .body(response);
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<List<FileResponseDto>> delete(
            Principal principal, @PathVariable Long docId, @PathVariable Long fileId) {
        List<FileResponseDto> response = fileService.deleteFile(principal.getName(), docId, fileId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
