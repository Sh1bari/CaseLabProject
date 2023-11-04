package com.example.caselabproject.controllers;

import com.example.caselabproject.exceptions.AppError;
import com.example.caselabproject.models.DTOs.request.UserCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.UserUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.*;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The type User controller.
 */
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/user")
@SecurityRequirement(name = "bearerAuth")
@Validated
public class UserController {

    private final UserService userService;


    /**
     * Gets user by id.
     *
     * @param id the id
     * @return the user by id
     * @author Igor Golovkov
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
     * Creates user.
     *
     * @param userRequestDto the user request dto
     * @return the response entity
     * @author Igor Golovkov
     */
    @Operation(summary = "Create new user, secured by admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserCreateResponseDto.class))}),
            @ApiResponse(responseCode = "409", description = "User exists",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))})})
    @PostMapping("/")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UserCreateResponseDto> createUser(
            @RequestBody @Valid UserCreateRequestDto userRequestDto) {
        UserCreateResponseDto userResponseDto = userService.create(userRequestDto);
        return ResponseEntity
                .created(URI.create("api/user/" + userResponseDto.getId()))
                .body(userResponseDto);
    }


    /**
     * Updates user by id.
     *
     * @param id             the id of user to get
     * @param userRequestDto the user request dto
     * @return the response entity
     * @author Igor Golovkov
     */
    @PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UserUpdateResponseDto> updateUserById(
            @PathVariable("id") @Min(value = 1L, message = "Id can't be less than 1") Long id,
            @RequestBody @Valid UserUpdateRequestDto userRequestDto) {
        UserUpdateResponseDto userUpdateResponseDto = userService.updateById(id, userRequestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userUpdateResponseDto);
    }


    /**
     * Deletes user by id.
     *
     * @param id the id
     * @return the response entity
     * @author Igor Golovkov
     */
    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> deleteUserById(
            @PathVariable("id") @Min(value = 1L, message = "Id can't be less than 1") Long id) {
        userService.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    /**
     * Recovers user by id.
     *
     * @param id the id
     * @return the response entity
     * @author Igor Golovkov
     */
    @PostMapping("/{id}/recover")
    //@Secured("ROLE_ADMIN")
    public ResponseEntity<UserRecoverResponseDto> recoverUserById(
            @PathVariable("id") @Min(value = 1L, message = "Id can't be less than 1") Long id) {
        UserRecoverResponseDto res = userService.recoverById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }


    /**
     * Gets docs by creator id.
     *
     * @param creatorId                 the creator id
     * @param name                      the name
     * @param creationDateFrom          the creation date from
     * @param creationDateTo            the creation date to
     * @param documentConstructorTypeId the document constructor type id
     * @param recordState               the record state
     * @param limit                     the limit
     * @param page                      the page
     * @return the docs by creator id
     * @author Igor Golovkov
     */
    @GetMapping("/{id}/docs")
    public ResponseEntity<List<DocumentCreateResponseDto>> getDocsByCreatorId(
            @PathVariable("id") @Min(value = 1L, message = "Id can't be less than 1") Long creatorId,
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            @RequestParam(name = "dateFrom", required = false) LocalDateTime creationDateFrom,
            @RequestParam(name = "dateTo", required = false) LocalDateTime creationDateTo,
            @RequestParam(name = "constrType", required = false) Long documentConstructorTypeId,
            @RequestParam(name = "recordState", required = false, defaultValue = "ACTIVE") RecordState recordState,
            @RequestParam(name = "limit", required = false, defaultValue = "30") Integer limit,
            @RequestParam(name = "page", defaultValue = "0") Integer page
    ) {
        List<DocumentCreateResponseDto> documentCreateResponseDto = userService.findDocsByFiltersByPage(
                creatorId,
                name,
                creationDateFrom,
                creationDateTo,
                documentConstructorTypeId,
                recordState,
                PageRequest.of(page, limit)
        );
        if (documentCreateResponseDto.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(documentCreateResponseDto);
    }

    /**
     * Gets all users by filters.
     *
     * @param roleName       the role name
     * @param departmentName the department name
     * @param firstName      the first name
     * @param lastName       the last name
     * @param patronymic     the patronymic
     * @param birthDateFrom  the birthdate from
     * @param birthDateTo    the birthdate to
     * @param email          the email
     * @param limit          the limit
     * @param page           the page
     * @return the all users by filters
     * @author Igor Golovkov
     */
    @GetMapping("/")
    public ResponseEntity<List<UserGetByIdResponseDto>> getAllUsersByFilters(
            @RequestParam(name = "roleName", required = false, defaultValue = "") String roleName,
            @RequestParam(name = "departmentName", required = false, defaultValue = "") String departmentName,
            @RequestParam(name = "firstName", required = false, defaultValue = "") String firstName,
            @RequestParam(name = "lastName", required = false, defaultValue = "") String lastName,
            @RequestParam(name = "patronymic", required = false, defaultValue = "") String patronymic,
            @RequestParam(name = "birthDateFrom", required = false, defaultValue = "1970-01-01") LocalDate birthDateFrom,
            @RequestParam(name = "birthDateTo", required = false, defaultValue = "3000-01-01") LocalDate birthDateTo,
            @RequestParam(name = "email", required = false, defaultValue = "") String email,
            @RequestParam(name = "limit", required = false, defaultValue = "30") Integer limit,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page
    ) {
        List<UserGetByIdResponseDto> userGetByIdResponseDtoList = userService.findAllUsersByFiltersByPage(
                roleName,
                departmentName,
                firstName,
                lastName,
                patronymic,
                birthDateFrom,
                birthDateTo,
                email,
                PageRequest.of(page, limit)
        );
        if (userGetByIdResponseDtoList.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userGetByIdResponseDtoList);
    }
}
