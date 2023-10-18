package com.example.caselabproject.controllers;

import com.example.caselabproject.dtos.JwtRequest;
import com.example.caselabproject.dtos.RegistrationUserDto;
import com.example.caselabproject.services.security.SecurityAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final SecurityAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        return authService.createAuthToken(authRequest);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto registrationUserDto) {
        return authService.createNewUser(registrationUserDto);
    }

    @PostMapping("/reset-token")
    public ResponseEntity<?> resetToken(Principal principal) {
        return authService.resetToken(principal.getName());
    }
}