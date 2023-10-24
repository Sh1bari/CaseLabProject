package com.example.caselabproject.controllers;

import com.example.caselabproject.models.DTOs.request.DocumentConstructorTypeRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentConstructorTypeResponseDto;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.Status;
import com.example.caselabproject.repositories.UserRepository;
import com.example.caselabproject.services.DocumentConstructorTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/doctype")
public class DocumentConstructorTypeController {
    private final DocumentConstructorTypeService typeService;
    private final UserRepository userRepository;

    @PostMapping()
    @Secured("ROLE_ADMIN")
    public ResponseEntity<DocumentConstructorTypeResponseDto> createDocumentType(
            @RequestBody DocumentConstructorTypeRequestDto documentTypeRequestDto){
        DocumentConstructorTypeResponseDto responseDto = typeService.create(documentTypeRequestDto);
        return ResponseEntity
                .created(URI.create("/api/doctype/" + responseDto.getId()))
                .body(responseDto);
    }
    @GetMapping("/")
    private User f(){
        User user = new User();
        //user.setStatus(Status.ACTIVE);
        user.setUsername("qwe");
        userRepository.save(user);
        return user;
    }
}
