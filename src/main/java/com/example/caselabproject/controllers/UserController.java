package com.example.caselabproject.controllers;

import com.example.caselabproject.models.DTOs.request.UserCreateRequestDto;
import com.example.caselabproject.models.DTOs.response.UserCreateResponseDto;
import com.example.caselabproject.models.DTOs.response.UserGetByIdResponseDto;
import com.example.caselabproject.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.net.URI;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/user")
@Validated
public class UserController {

    private final UserService userService;

    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserGetByIdResponseDto> getUserById(
            @PathVariable("id") @Min(value = 1L, message = "Id cant be less than 1") Long id) {
        UserGetByIdResponseDto userResponseDto = userService.getById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userResponseDto);
    }

    /**
     * добавить msg для валидации!!
     * @param userRequestDto
     * @return
     */
    @PostMapping("/")
    //@Secured("ROLE_ADMIN")
    public ResponseEntity<UserCreateResponseDto> createUser(
            @RequestBody @Valid UserCreateRequestDto userRequestDto) {
        UserCreateResponseDto userResponseDto = userService.create(userRequestDto);
        return ResponseEntity
                .created(URI.create("api/user/" + userResponseDto.getId()))
                .body(userResponseDto);
    }

    /*@PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UserResponseDto> updateUserById(
            @PathVariable("id") @Min(value = 1L, message = "Id cant be less than 1") Long id,
            @RequestBody UserUpdateRequestDto userRequestDto) {
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
    }*/


    /*@GetMapping("/{id}/docs")
    public ResponseEntity<UserResponseDto> getDocsByCreatorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(userService.findByCreatorId(id));
    }*/
}
