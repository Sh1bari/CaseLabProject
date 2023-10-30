package com.example.caselabproject.controllers;

import com.example.caselabproject.models.DTOs.request.UserCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.UserUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.*;
import com.example.caselabproject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/user")
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserGetByIdResponseDto> getUserById(
            @PathVariable("id") @Min(value = 1L, message = "Id can't be less than 1") Long id) {
        UserGetByIdResponseDto userResponseDto = userService.getById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userResponseDto);
    }

    @PostMapping("/")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UserCreateResponseDto> createUser(
            @RequestBody @Valid UserCreateRequestDto userRequestDto) {
        UserCreateResponseDto userResponseDto = userService.create(userRequestDto);
        return ResponseEntity
                .created(URI.create("api/user/" + userResponseDto.getId()))
                .body(userResponseDto);
    }

    @PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UserUpdateResponseDto> updateUserById(
            @PathVariable("id") @Min(value = 1L, message = "Id can't be less than 1") Long id,
            @RequestBody UserUpdateRequestDto userRequestDto) {
        UserUpdateResponseDto userUpdateResponseDto = userService.updateById(id, userRequestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userUpdateResponseDto);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UserDeleteResponseDto> deleteUserById(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }


    @GetMapping("/{id}/docs")
    public ResponseEntity<List<DocumentCreateResponseDto>> getDocsByCreatorId(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.findDocsByCreatorId(id));
    }
}
