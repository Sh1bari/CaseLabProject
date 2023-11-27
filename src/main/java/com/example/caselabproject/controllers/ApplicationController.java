package com.example.caselabproject.controllers;

import com.example.caselabproject.exceptions.AppError;
import com.example.caselabproject.models.DTOs.request.ApplicationCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.ApplicationUpdateRequestDto;
import com.example.caselabproject.models.DTOs.request.DocIdRequestDto;
import com.example.caselabproject.models.DTOs.response.ApplicationCreateResponseDto;
import com.example.caselabproject.models.DTOs.response.ApplicationFindResponseDto;
import com.example.caselabproject.models.DTOs.response.ApplicationUpdateResponseDto;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;

/**
 * Description:
 *
 * @author Tribushko Danil
 */
@Validated
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/application")
@SecurityRequirement(name = "bearerAuth")
public class ApplicationController {
    private final ApplicationService applicationService;

    @Operation(summary = "Create new application", description = "Secured by authorized users")
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
            @RequestBody @Valid ApplicationCreateRequestDto requestDto) {
        ApplicationCreateResponseDto responseDto = applicationService.createApplication(principal.getName(), requestDto);
        return ResponseEntity
                .created(URI.create("/api/application" + responseDto.getId()))
                .body(responseDto);
    }

    @Operation(summary = "Update application, secured by user", description = "Secured by authorized users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Application updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApplicationUpdateResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "User by provided id username not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))}),
            @ApiResponse(responseCode = "400", description = "Deadline cant be less than now",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))})

    })
    @PreAuthorize("@applicationSecurityService.canUpdateApplication(#principal.getName, #id)")
    @Secured("ROLE_USER")
    @PutMapping("/{id}")
    public ResponseEntity<ApplicationUpdateResponseDto> update(
            Principal principal,
            @PathVariable @Min(value = 1L, message = "Id cant be less than 1") Long id,
            @RequestBody @Valid ApplicationUpdateRequestDto requestDto) {
        ApplicationUpdateResponseDto responseDto = applicationService.updateApplication(id, principal.getName(),
                requestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

    @Operation(summary = "Delete application", description = "Secured by authorized users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Application deleted",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Application with provided id is not found/" +
                    "User with provided id is not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))}),
            @ApiResponse(responseCode = "403", description = "User with provided id is not creator",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))}),
            @ApiResponse(responseCode = "409", description = "Application already deleted")
    })
    @Secured("ROLE_USER")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            Principal principal,
            @PathVariable @Min(value = 1L, message = "Id cant be less than 1") Long id) {
        applicationService.deleteApplication(id, principal.getName());

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Operation(summary = "Get application by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Application found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApplicationFindResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "Application with provided id is not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))})
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationFindResponseDto> findApplicationById(
            @PathVariable @Min(value = 1L, message = "Id cant be less than 1") Long id) {
        ApplicationFindResponseDto responseDto = applicationService.getApplicationById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

    @Operation(summary = "Connect document to application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document connected",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApplicationFindResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "Application with provided id is not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))})
    })
    @PatchMapping("/{id}/doc")
    public ResponseEntity<ApplicationFindResponseDto> connectDocToApplication(
            @PathVariable @Min(value = 1L, message = "Id cant be less than 1") Long id,
            @RequestBody @Valid DocIdRequestDto req) {
        ApplicationFindResponseDto responseDto = applicationService.connectDocToApplication(id, req);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

}
