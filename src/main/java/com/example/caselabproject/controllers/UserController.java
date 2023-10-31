package com.example.caselabproject.controllers;

import com.example.caselabproject.models.DTOs.request.UserCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.UserUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.*;
import com.example.caselabproject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    /**
     * Description:
     *
     * @author
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserGetByIdResponseDto> getUserById(
            @PathVariable("id") @Min(value = 1L, message = "Id can't be less than 1") Long id) {
        UserGetByIdResponseDto userResponseDto = userService.getById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userResponseDto);
    }

    /**
     * Description:
     *
     * @author
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

    /**
     * Description:
     *
     * @author
     */
    @PutMapping("/{id}")
    //@Secured("ROLE_ADMIN")
    public ResponseEntity<UserUpdateResponseDto> updateUserById(
            @PathVariable("id") @Min(value = 1L, message = "Id can't be less than 1") Long id,
            @RequestBody @Valid UserUpdateRequestDto userRequestDto) {
        UserUpdateResponseDto userUpdateResponseDto = userService.updateById(id, userRequestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userUpdateResponseDto);
    }

    /**
     * Description:
     *
     * @author
     */
    @DeleteMapping("/{id}")
    //@Secured("ROLE_ADMIN")
    public ResponseEntity<?> deleteUserById(
            @PathVariable("id") @Min(value = 1L, message = "Id can't be less than 1") Long id) {
        userService.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PostMapping("/{id}/recover")
    //@Secured("ROLE_ADMIN")
    public ResponseEntity<UserRecoverResponseDto> recoverUserById(
            @PathVariable("id") @Min(value = 1L, message = "Id can't be less than 1") Long id) {
        UserRecoverResponseDto res = userService.recoverById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }


    @GetMapping("/{id}/docs")
    public ResponseEntity<List<DocumentCreateResponseDto>> getDocsByCreatorId(
            @PathVariable("id") @Min(value = 1L, message = "Id can't be less than 1") Long id,
            @RequestParam(name = "limit", required = false, defaultValue = "30") Integer limit,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "name", required = false, defaultValue = "") String name) {
        List<DocumentCreateResponseDto> documentCreateResponseDto = userService.findDocsByCreatorIdByPage(id, name, PageRequest.of(page, limit));
        if (documentCreateResponseDto.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }//
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(documentCreateResponseDto);
    }
}
