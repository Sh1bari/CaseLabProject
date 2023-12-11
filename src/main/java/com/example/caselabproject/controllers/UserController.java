package com.example.caselabproject.controllers;

import com.example.caselabproject.exceptions.AppError;
import com.example.caselabproject.models.DTOs.request.user.UserCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.user.UserUpdatePasswordRequest;
import com.example.caselabproject.models.DTOs.request.user.UserUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentGetAllResponse;
import com.example.caselabproject.models.DTOs.response.application.ApplicationFindResponseDto;
import com.example.caselabproject.models.DTOs.response.application.ApplicationItemGetByIdResponseDto;
import com.example.caselabproject.models.DTOs.response.user.UserCreateResponseDto;
import com.example.caselabproject.models.DTOs.response.user.UserGetByIdResponseDto;
import com.example.caselabproject.models.DTOs.response.user.UserRecoverResponseDto;
import com.example.caselabproject.models.DTOs.response.user.UserUpdateResponseDto;
import com.example.caselabproject.models.enums.ApplicationItemStatus;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.services.DocumentConstructorTypeService;
import com.example.caselabproject.services.UserService;
import com.example.caselabproject.validation.annotations.CheckOrganization;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
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
    @Operation(summary = "Find user with given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User got",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserGetByIdResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "User with given id not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))})})
    @GetMapping("/{id}")
    public ResponseEntity<UserGetByIdResponseDto> getUserById(
            @CheckOrganization(serviceClass = UserService.class)
            @PathVariable("id") @Min(value = 1L, message = "Id can't be less than 1") Long id) {
        UserGetByIdResponseDto userResponseDto = userService.getById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userResponseDto);
    }

    @Operation(summary = "Get yourself")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Your profile",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserGetByIdResponseDto.class))})})
    @GetMapping("/me")
    @Secured("ROLE_USER")
    public ResponseEntity<UserGetByIdResponseDto> getMyId(
            Principal principal) {
        UserGetByIdResponseDto userResponseDto = userService.getByUsername(principal.getName());
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
     * Updates user password by id.
     *
     * @param id             the id of user to get
     * @param userRequestDto the user request dto
     * @return the response entity
     * @author Igor Golovkov
     */
    @Operation(summary = "Update existing user with given id, secured by admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserUpdateResponseDto.class))}),
            @ApiResponse(responseCode = "409", description = "User with given data exists",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))}),
            @ApiResponse(responseCode = "404", description = "User with given id not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))})})
    @PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UserUpdateResponseDto> updateUserById(
            @CheckOrganization(serviceClass = UserService.class)
            @PathVariable("id") @Min(value = 1L, message = "Id can't be less than 1") Long id,
            @RequestBody @Valid UserUpdateRequestDto userRequestDto) {
        UserUpdateResponseDto userUpdateResponseDto = userService.updateById(id, userRequestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userUpdateResponseDto);
    }

    @Operation(summary = "Update existing user password with given id, secured by admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserUpdateResponseDto.class))}),
            @ApiResponse(responseCode = "409", description = "User with given data exists",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))}),
            @ApiResponse(responseCode = "404", description = "User with given id not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))})})
    @PutMapping("/{id}/password")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UserUpdateResponseDto> updateUserPasswordById(
            @PathVariable("id") @Min(value = 1L, message = "Id can't be less than 1") Long id,
            @RequestBody @Valid UserUpdatePasswordRequest req) {
        UserUpdateResponseDto res = userService.updatePasswordById(id, req);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }


    /**
     * Deletes user by id.
     *
     * @param id the id
     * @return the response entity
     * @author Igor Golovkov
     */
    @Operation(summary = "Change record state of user with given id to DELETED, secured by admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User's record state changed to DELETED",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseEntity.class))}),
            @ApiResponse(responseCode = "404", description = "User with given id not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))})})
    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> deleteUserById(
            @CheckOrganization(serviceClass = UserService.class)
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
    @Operation(summary = "Change record state of user with given id to ACTIVE, secured by admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User's record state changed to ACTIVE",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserRecoverResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "User with given id not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))})})
    @PostMapping("/{id}/recover")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UserRecoverResponseDto> recoverUserById(
            @CheckOrganization(serviceClass = UserService.class)
            @PathVariable("id") @Min(value = 1L, message = "Id can't be less than 1") Long id) {
        UserRecoverResponseDto res = userService.recoverById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }

    /**
     * Appoints new director by id, transfers application items of former director to new director.
     *
     * @param departmentId id of department of former director and new director
     * @param userId       new director's id
     * @return the response entity
     * @author Igor Golovkov
     */
    @Operation(summary = "Appoints new director by his and department's id, transfers to him application items of former director if he exists, secured by admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Director is appointed",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserUpdateResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "User with given id not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))})})
    @PutMapping("department/{departmentId}/user/{userId}/appointDirector")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UserUpdateResponseDto> appointDirectorById(
            @PathVariable("departmentId") @Min(value = 1L, message = "Department id can't be less than 1") Long departmentId,
            @PathVariable("userId") @Min(value = 1L, message = "User id can't be less than 1") Long userId) {
        UserUpdateResponseDto userUpdateResponseDto = userService.appointDirector(departmentId, userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userUpdateResponseDto);
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
    @Operation(summary = "Get all documents of user (creator) with given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Documents got",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class))}),
            @ApiResponse(responseCode = "204", description = "Documents of user with given id not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))}),
            @ApiResponse(responseCode = "404", description = "User with given id not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))})})
    @GetMapping("/{id}/docs")
    @Secured("ROLE_USER")
    public ResponseEntity<List<DocumentGetAllResponse>> getDocsByCreatorId(
            @PathVariable("id") @Min(value = 1L, message = "Id can't be less than 1") Long creatorId,
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            @RequestParam(name = "dateFrom", required = false, defaultValue = "1970-01-01T00:00:00") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime creationDateFrom,
            @RequestParam(name = "dateTo", required = false, defaultValue = "3000-01-01T23:59:59") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime creationDateTo,
            @CheckOrganization(serviceClass = DocumentConstructorTypeService.class)
            @RequestParam(name = "constrType", required = false) Long documentConstructorTypeId,
            @RequestParam(name = "recordState", required = false, defaultValue = "ACTIVE") RecordState recordState,
            @RequestParam(name = "limit", required = false, defaultValue = "30") @Min(value = 1L, message = "Page limit can't be less than 1") Integer limit,
            @RequestParam(name = "page", required = false, defaultValue = "0") @Min(value = 0L, message = "Page number can't be less than 0") Integer page
    ) {
        List<DocumentGetAllResponse> documentCreateResponseDto = userService.findDocsByFiltersByPage(
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
    @Operation(summary = "Get all users filtered by values of their attributes or not filtered if filters not given")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users got",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class))}),
            @ApiResponse(responseCode = "204", description = "Users not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))})})
    @GetMapping("/")
    public ResponseEntity<List<UserGetByIdResponseDto>> getAllUsersByFilters(
            @RequestParam(name = "roleName", required = false, defaultValue = "") String roleName,
            @RequestParam(name = "departmentName", required = false, defaultValue = "") String departmentName,
            @RequestParam(name = "firstName", required = false, defaultValue = "") String firstName,
            @RequestParam(name = "lastName", required = false, defaultValue = "") String lastName,
            @RequestParam(name = "patronymic", required = false, defaultValue = "") String patronymic,
            @RequestParam(name = "birthDateFrom", required = false, defaultValue = "1970-01-01") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthDateFrom,
            @RequestParam(name = "birthDateTo", required = false, defaultValue = "3000-01-01") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthDateTo,
            @RequestParam(name = "email", required = false, defaultValue = "") String email,
            @RequestParam(name = "limit", required = false, defaultValue = "30") @Min(value = 1L, message = "Page limit can't be less than 1") Integer limit,
            @RequestParam(name = "page", required = false, defaultValue = "0") @Min(value = 0L, message = "Page number can't be less than 0") Integer page
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
                PageRequest.of(page, limit));
        if (userGetByIdResponseDtoList.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userGetByIdResponseDtoList);
    }

    @Operation(summary = "Get user's application by page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Page with applications found",
                    content = {@Content(mediaType = "applications/json",
                            schema = @Schema(implementation = ApplicationFindResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "User by provided id not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))})
    })
    @GetMapping(value = "/{id}/applications")
    @Secured("ROLE_USER")
    public ResponseEntity<?> getApplicationsByCreatorId(
            @CheckOrganization(serviceClass = UserService.class)
            @PathVariable("id") @Min(value = 1L, message = "Id can't be less than 1") Long id,
            @RequestParam(name = "limit", required = false, defaultValue = "30") @Min(value = 1L, message = "Page limit can't be less than 1") Integer limit,
            @RequestParam(name = "page", defaultValue = "0") @Min(value = 0L, message = "Page number can't be less than 0") Integer page,
            @RequestParam(name = "recordState", required = false, defaultValue = "ACTIVE") RecordState recordState) {
        List<ApplicationFindResponseDto> applicationFindResponseDto = userService.findApplicationsByCreatorIdByPage(id, recordState, PageRequest.of(page, limit));
        if (applicationFindResponseDto.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(applicationFindResponseDto);
        }
    }

    @Operation(summary = "Get application items by user id", description = "Secured by authorized users, can be read only by admins, creator or employees in the same department")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Application items by user id",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApplicationItemGetByIdResponseDto.class))}),
            @ApiResponse(responseCode = "204", description = "No content with these filters found",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", description = "User don't have enough rights for access to Application items",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))}),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))})})
    @GetMapping("/{id}/applicationItems")
    @Secured("ROLE_USER")
    public ResponseEntity<List<ApplicationItemGetByIdResponseDto>> getApplicationItemsByUserId(
            @CheckOrganization(serviceClass = UserService.class)
            @PathVariable("id") @Min(value = 1L, message = "Id can't be less than 1") Long id,
            @RequestParam(name = "applicationName", required = false, defaultValue = "") String applicationName,
            @RequestParam(name = "status", required = false) ApplicationItemStatus status,
            @RequestParam(name = "recordState", required = false, defaultValue = "ACTIVE") RecordState recordState,
            @RequestParam(name = "limit", required = false, defaultValue = "30") @Min(value = 1L, message = "Page limit can't be less than 1") Integer limit,
            @RequestParam(name = "page", defaultValue = "0") @Min(value = 0L, message = "Page number can't be less than 0") Integer page,
            Principal principal) {

        List<ApplicationItemGetByIdResponseDto> applicationItemGetByIdResponseDtoList = userService
                .findApplicationItemsByUserIdByPage(
                        id,
                        applicationName,
                        status,
                        recordState,
                        PageRequest.of(page, limit),
                        principal.getName());
        if (applicationItemGetByIdResponseDtoList.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(applicationItemGetByIdResponseDtoList);
        }
    }
}
