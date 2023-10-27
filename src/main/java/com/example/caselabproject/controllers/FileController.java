package com.example.caselabproject.controllers;

import com.example.caselabproject.models.DTOs.request.FileCreateRequestDto;
import com.example.caselabproject.models.DTOs.response.FileCreateResponseDto;
import com.example.caselabproject.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("doc/{id}/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/")
    ResponseEntity<FileCreateResponseDto> add(
            @RequestBody FileCreateRequestDto requestDto, @PathVariable String id) {
        FileCreateResponseDto responseDto = fileService.addFile(requestDto);
        return  ResponseEntity
                .created(URI.create("/api/doc/{id}/file/" + responseDto.getId()))
                .body(responseDto);
    }
}
