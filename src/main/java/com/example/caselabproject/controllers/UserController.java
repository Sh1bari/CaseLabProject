package com.example.caselabproject.controllers;

import com.example.caselabproject.models.DTOs.request.UserRequestDto;
import com.example.caselabproject.models.DTOs.response.UserResponseDto;
import com.example.caselabproject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable("id") Long id) {
        UserResponseDto userResponseDto = userService.getById(id);
        return ResponseEntity
                .ok(userResponseDto);
    }

    @PostMapping("/")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto userRequestDto) {
        UserResponseDto userResponseDto = userService.create(userRequestDto);
        return ResponseEntity
                .created(URI.create("api/user/" + userResponseDto.getId()))
                .body(userResponseDto);
    }

    @PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UserResponseDto> updateUserById(@PathVariable("id") Long id, @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto userResponseDto = userService.updateById(id, userRequestDto);
        return ResponseEntity
                .ok(userResponseDto);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UserResponseDto> deleteUserById(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return ResponseEntity
                .noContent()
                .build();
    }


    /*@GetMapping("/{id}/docs")
    public ResponseEntity<UserResponseDto> getDocsByCreatorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(userService.findByCreatorId(id));
    }*/
}
