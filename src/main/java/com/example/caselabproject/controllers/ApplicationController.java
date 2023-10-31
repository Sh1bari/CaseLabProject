package com.example.caselabproject.controllers;

import com.example.caselabproject.exceptions.AppError;
import com.example.caselabproject.models.DTOs.request.ApplicationCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.ApplicationDeleteRequestDto;
import com.example.caselabproject.models.DTOs.request.ApplicationUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.*;
import com.example.caselabproject.services.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/application")
@SecurityRequirement(name = "bearerAuth")
public class ApplicationController {
    //TODO validation

    private final ApplicationService applicationService;

    /**
     * Description:
     * @author
     */
    @Operation(summary = "Create new application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Application created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApplicationCreateResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "User by username not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))}),
            @ApiResponse(responseCode = "400", description = "Deadline cant be less than now",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))})})
    @PostMapping("/")
    @Secured("ROLE_USER")
    public ResponseEntity<ApplicationCreateResponseDto> create(
            Principal principal,
            @RequestBody @Valid ApplicationCreateRequestDto requestDto){
        ApplicationCreateResponseDto responseDto = applicationService.createApplication(principal.getName(), requestDto);
        return ResponseEntity
                .created(URI.create("/api/application" + responseDto.getId()))
                .body(responseDto);
    }
    @Secured("ROLE_USER")
    @PutMapping("/{id}")
    public ResponseEntity<ApplicationUpdateResponseDto> update(
            Principal principal,
            @PathVariable @Min(value = 1L, message = "Id cant be less than 1") Long id,
            @RequestBody @Valid ApplicationUpdateRequestDto requestDto){
        ApplicationUpdateResponseDto responseDto = applicationService.updateApplication(id, principal.getName(),
                requestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

    @Secured("ROLE_USER")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApplicationDeleteResponseDto> delete(
            Principal principal,
            @PathVariable @Min(value = 1L, message = "Id cant be less than 1") Long id){
        ApplicationDeleteResponseDto responseDto = applicationService.deleteApplication(id, principal.getName());

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(responseDto);
    }
    @Operation(summary = "Get application by id")
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationFindResponseDto> findApplicationById(
            @PathVariable @Min(value = 1L, message = "Id cant be less than 1") Long id) {
        ApplicationFindResponseDto responseDto = applicationService.getApplicationById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }
}
