package com.example.caselabproject.controllers;

import com.example.caselabproject.models.DTOs.request.ApplicationCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.ApplicationDeleteRequestDto;
import com.example.caselabproject.models.DTOs.request.ApplicationUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.ApplicationCreateResponseDto;
import com.example.caselabproject.models.DTOs.response.ApplicationDeleteResponseDto;
import com.example.caselabproject.models.DTOs.response.ApplicationFindResponseDto;
import com.example.caselabproject.models.DTOs.response.ApplicationUpdateResponseDto;
import com.example.caselabproject.services.implementations.ApplicationServiceImpl;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/application")
public class ApplicationController {
    private ApplicationServiceImpl applicationService;
    private static final String PATH = "/api/application";

    @Autowired
    public ApplicationController(ApplicationServiceImpl applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("/")
    public ResponseEntity<ApplicationCreateResponseDto> create(
            @RequestBody ApplicationCreateRequestDto requestDto
    ) {
        ApplicationCreateResponseDto responseDto = applicationService.createApplication(requestDto);
        return ResponseEntity
                .created(URI.create(PATH + responseDto.getId()))
                .body(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationUpdateResponseDto> update(@PathVariable Long id,
            @RequestBody ApplicationUpdateRequestDto requestDto
    ) {
        ApplicationUpdateResponseDto responseDto = applicationService.updateApplication(id, requestDto);
        return ResponseEntity
                .created(URI.create(PATH + responseDto.getId()))
                .body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApplicationDeleteResponseDto> delete(
            @RequestBody ApplicationDeleteRequestDto requestDto
    ) {
        ApplicationDeleteResponseDto responseDto = applicationService.deleteApplication(requestDto);

        return ResponseEntity
                .created(URI.create(PATH + responseDto.getId()))
                .body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationFindResponseDto> findById(
            @PathVariable Long id
    ) {
        ApplicationFindResponseDto responseDto = applicationService.getApplicationById(id);

        return ResponseEntity
                .created(URI.create(PATH + responseDto.getId()))
                .body(responseDto);
    }
}
