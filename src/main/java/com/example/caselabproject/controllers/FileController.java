package com.example.caselabproject.controllers;

import com.example.caselabproject.models.DTOs.request.FileCreateRequestDto;
import com.example.caselabproject.models.DTOs.response.FileResponseDto;
import com.example.caselabproject.models.entities.File;
import com.example.caselabproject.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("doc/{docId}/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/")
    ResponseEntity<List<FileResponseDto>> add(
            Principal principal, @RequestBody FileCreateRequestDto request, @PathVariable Long docId) {
        List<FileResponseDto> response = fileService.addFile(principal.getName(), request, docId);
        return  ResponseEntity
                .created(URI.create("/api/doc/" + docId + "/file/"))
                .body(response);
    }

    @DeleteMapping("/{fileId}")
    ResponseEntity<List<FileResponseDto>> delete(
            Principal principal, @PathVariable Long docId, @PathVariable Long fileId) {
        List<FileResponseDto> response = fileService.deleteFile(principal.getName(), docId, fileId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
