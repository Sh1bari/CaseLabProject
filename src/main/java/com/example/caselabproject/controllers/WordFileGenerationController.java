package com.example.caselabproject.controllers;

import com.example.caselabproject.services.WordFileGenerator;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @GetMapping("/doc/{id}/generate")
    public ResponseEntity<Resource> generateAndDownloadFile(@PathVariable("id") Long id) {
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
