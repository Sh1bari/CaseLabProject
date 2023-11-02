package com.example.caselabproject.controllers;

import com.example.caselabproject.models.DTOs.response.FileDownloadResponseDto;
import com.example.caselabproject.models.DTOs.response.FileResponseDto;
import com.example.caselabproject.services.FileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
            @RequestParam("file") MultipartFile file,
            Principal principal,
            @PathVariable @Min(value = 1L, message = "Id can't be less than 1") Long docId) {
        List<FileResponseDto> response = fileService.addFile(principal.getName(), file, docId);
        return ResponseEntity
                .created(URI.create("/api/doc/" + docId + "/file/"))
                .body(response);
    }

    @GetMapping("/")
    public ResponseEntity<List<FileResponseDto>> getFilesByDocumentId(
            @PathVariable @Min(value = 1L, message = "Id can't be less than 1") Long docId,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "offset", required = false, defaultValue = "20") Integer offset) {
        List<FileResponseDto> response = fileService.getFiles(docId, PageRequest.of(page, offset));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{fileId}/download")
    public ResponseEntity<?> downloadFile(@PathVariable @Min(value = 1L, message = "Id can't be less than 1") Long docId,
                                          @PathVariable @Min(value = 1L, message = "Id can't be less than 1") Long fileId) {
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

    @PutMapping("/{fileId}")
    public ResponseEntity<List<FileResponseDto>> updateFileByDocumentId(
            @RequestParam("file") MultipartFile file,
            Principal principal,
            @PathVariable @Min(value = 1L, message = "Id can't be less than 1") Long docId,
            @PathVariable @Min(value = 1L, message = "Id can't be less than 1") Long fileId) {
        List<FileResponseDto> response = fileService.updateFile(principal.getName(), file, docId, fileId);
        return ResponseEntity
                .created(URI.create("/api/doc/" + docId + "/file/"))
                .body(response);
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<List<FileResponseDto>> deleteFile(
            Principal principal,
            @PathVariable @Min(value = 1L, message = "Id can't be less than 1") Long docId,
            @PathVariable @Min(value = 1L, message = "Id can't be less than 1") Long fileId) {
        List<FileResponseDto> response = fileService.deleteFile(principal.getName(), docId, fileId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
