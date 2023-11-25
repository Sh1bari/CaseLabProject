package com.example.caselabproject.controllers;

import com.example.caselabproject.exceptions.AppError;
import com.example.caselabproject.models.DTOs.JwtRequest;
import com.example.caselabproject.models.DTOs.JwtResponse;
import com.example.caselabproject.models.DTOs.RegistrationUserDto;
import com.example.caselabproject.models.DTOs.UserDto;
import com.example.caselabproject.services.security.SecurityAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class AuthController {
    private final SecurityAuthService authService;

    @Operation(summary = "Login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful authorization",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = JwtResponse.class))
                    }),
            @ApiResponse(responseCode = "401", description = "Incorrect login or password",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppError.class))
                    }),
            @ApiResponse(responseCode = "403", description = "User has been banned",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppError.class))
                    })
    })
    @PostMapping("/login")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        return authService.createAuthToken(authRequest);
    }

    @Operation(summary = "Registration for admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful registration",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Password mismatch or user exists",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppError.class))
                    })
    })
    @PostMapping("/registration")
    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto registrationUserDto) {
        return authService.createNewUser(registrationUserDto);
    }

    @Operation(summary = "Reset JWT token, only for authorized users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = JwtResponse.class))
                    }),
            @ApiResponse(responseCode = "401", description = "Token timeout",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppError.class))
                    }),
            @ApiResponse(responseCode = "403", description = "User has been banned",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppError.class))
                    })
    })
    @SecurityRequirement(name = "bearerAuth")
    @Secured("ROLE_USER")
    @PostMapping("/reset-token")
    public ResponseEntity<?> resetToken(Principal principal) {
        return authService.resetToken(principal.getName());
    }
}