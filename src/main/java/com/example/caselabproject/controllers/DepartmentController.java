package com.example.caselabproject.controllers;

import com.example.caselabproject.exceptions.AppError;
import com.example.caselabproject.models.DTOs.request.department.DepartmentChildDto;
import com.example.caselabproject.models.DTOs.request.department.DepartmentCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.department.DepartmentRequestDto;
import com.example.caselabproject.models.DTOs.response.application.ApplicationItemGetByIdResponseDto;
import com.example.caselabproject.models.DTOs.response.department.DepartmentCreateResponseDto;
import com.example.caselabproject.models.DTOs.response.department.DepartmentGetAllResponseDto;
import com.example.caselabproject.models.DTOs.response.department.DepartmentGetByIdResponseDto;
import com.example.caselabproject.models.DTOs.response.department.DepartmentUpdateResponseDto;
import com.example.caselabproject.models.DTOs.response.user.UserGetByIdResponseDto;
import com.example.caselabproject.models.enums.ApplicationItemStatus;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.services.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.List;


@RestController
@CrossOrigin
@RequestMapping("/department")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "bearerAuth")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Operation(summary = "Create new department, secured by admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Department created",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DepartmentCreateResponseDto.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppError.class))
                    }),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppError.class))
                    })
    })
    @PostMapping("/")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<DepartmentCreateResponseDto> createNewDepartment(
            @RequestBody @Valid DepartmentCreateRequestDto departmentRequestDto,
            Principal principal) {

        DepartmentCreateResponseDto responseDto = departmentService.create(departmentRequestDto, principal.getName());
        return ResponseEntity
                .created(URI.create("/api/department/" + responseDto.getId()))
                .body(responseDto);
    }

    @Operation(summary = "Delete a department, secured by admin", description = "Deletes a department by its ID, secured by admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Department deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid department ID",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppError.class))
                    }),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Department not found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppError.class))
                    })
    })
    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> deleteDepartment(
            @PathVariable @Min(value = 1L, message = "Id cant be less than 1") Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Operation(summary = "Recover a deleted department, secured by admin", description = "Recovers a deleted department by its ID, secured by admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Department recovered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid department ID",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppError.class))
                    }),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Department not found or not recoverable",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppError.class))
                    })
    })
    @PostMapping("/{id}/recover")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> recoverDepartment(
            @PathVariable @Min(value = 1L, message = "Id cant be less than 1") Long id) {
        departmentService.recoverDepartment(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Operation(summary = "Update department name by ID", description = "Updates the name of a specific department using its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Department name updated successfully",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DepartmentCreateResponseDto.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Invalid department ID or request format",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppError.class))
                    }),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppError.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Department not found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppError.class))
                    })
    })
    @PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<DepartmentUpdateResponseDto> updateDepartmentNameById(
            @PathVariable @Min(value = 1L, message = "Id cant be less than 1") Long id,
            @RequestBody @Valid DepartmentRequestDto departmentRequestDto) {
        DepartmentUpdateResponseDto responseDto = departmentService.updateName(id, departmentRequestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

    @Operation(summary = "Get a department by ID", description = "Retrieves details of a specific department by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Department found and returned successfully",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DepartmentCreateResponseDto.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Invalid department ID",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppError.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Department not found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppError.class))
                    })
    })
    @GetMapping("/{id}")
    @Secured("ROLE_USER")
    public ResponseEntity<DepartmentGetByIdResponseDto> getDepartmentById(
            @PathVariable @Min(value = 1L, message = "Id cant be less than 1") Long id) {
        DepartmentGetByIdResponseDto responseDto = departmentService.getById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

    @Operation(summary = "Add a child department to an existing department",
            description = "Adds a new child department to an existing department identified by the provided ID. Requires admin privileges.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Child department added successfully",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DepartmentGetByIdResponseDto.class))
                    }),
            @ApiResponse(responseCode = "400",
                    description = "Invalid request parameters or validation errors",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppError.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Department not found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppError.class))
                    }),
            @ApiResponse(responseCode = "403",
                    description = "Access denied for non-admin users",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppError.class))
                    })
    })
    @PatchMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<DepartmentGetByIdResponseDto> addChildDepartment(
            @PathVariable @Min(value = 1L, message = "Id cant be less than 1") Long id,
            @RequestBody @Valid DepartmentChildDto departmentChildDto) {

        DepartmentGetByIdResponseDto responseDto = departmentService.setParentDepartment(id, departmentChildDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }


    @Operation(summary = "Get all departments", description = "Retrieves a list of departments with pagination and optional filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of departments retrieved successfully",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DepartmentCreateResponseDto.class))
                    }),
            @ApiResponse(responseCode = "204", description = "No departments found"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppError.class))
                    }),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppError.class))
                    })
    })
    @GetMapping("/")
    @Secured("ROLE_USER")
    //TODO в пагинацию добавить сериал ключ
    //TODO получить департаменты в организации (Principal)
    //TODO все листы заменить на Page
    public ResponseEntity<Page<DepartmentGetAllResponseDto>> getAllDepartments(
            @RequestParam(name = "page", defaultValue = "0") @Min(value = 0, message = "Page cant be less than 0") Integer page,
            @RequestParam(name = "limit", defaultValue = "30") @Min(value = 1, message = "Page limit cant be less than 1") Integer limit,
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            @RequestParam(value = "recordState", required = false, defaultValue = "ACTIVE") RecordState recordState,
            @RequestParam(value = "serialKey", required = false, defaultValue = "") String serialKey,
            Principal principal) {
        Page<DepartmentGetAllResponseDto> responseDto = departmentService.getAllDepartmentsPageByPage(PageRequest.of(page, limit), name, recordState, serialKey, principal.getName());
        if (responseDto.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

    @Operation(summary = "Get all users in a department", description = "Retrieves a list of users within a specific department filtered by the user's record state")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users retrieved successfully",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserGetByIdResponseDto.class))
                    }),
            @ApiResponse(responseCode = "204", description = "No users found in the department"),
            @ApiResponse(responseCode = "400", description = "Invalid department ID",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppError.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Department not found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppError.class))
                    })
    })
    @GetMapping("/{id}/users")
    @Secured("ROLE_USER")
    //TODO в организации
    //TODO все листы заменить на Page
    public ResponseEntity<List<UserGetByIdResponseDto>> getAllUsersInDepartment(
            @PathVariable @Min(value = 1L, message = "Id cant be less than 1") Long id,
            @RequestParam(value = "recordState", required = false, defaultValue = "ACTIVE") RecordState recordState) {
        List<UserGetByIdResponseDto> responseDto = departmentService.getAllUsersFilteredByDepartment(recordState, id);
        if (responseDto.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

    @Operation(summary = "Get application items by department id", description = "Secured by authorized users, can be read only by admins or employees in the department")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Application items by department id",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApplicationItemGetByIdResponseDto.class))}),
            @ApiResponse(responseCode = "204", description = "No content with these filters found",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", description = "User don't have enough rights for access to Application items",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))}),
            @ApiResponse(responseCode = "404", description = "Department or user not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppError.class))})})
    @GetMapping("/{id}/applicationItems")
    @Secured("ROLE_USER")
    public ResponseEntity<List<ApplicationItemGetByIdResponseDto>> getApplicationItemsById(
            @PathVariable("id") @Min(value = 1L, message = "Id can't be less than 1") Long id,
            @RequestParam(name = "applicationName", required = false, defaultValue = "") String applicationName,
            @RequestParam(name = "status", required = false) ApplicationItemStatus status,
            @RequestParam(name = "recordState", required = false, defaultValue = "ACTIVE") RecordState recordState,
            @RequestParam(name = "limit", required = false, defaultValue = "30") @Min(value = 1L, message = "Page limit can't be less than 1") Integer limit,
            @RequestParam(name = "page", defaultValue = "0") @Min(value = 0L, message = "Page number can't be less than 0") Integer page,
            Principal principal) {
        List<ApplicationItemGetByIdResponseDto> res = departmentService
                .findApplicationItemsByDepartmentIdByPage(
                        id,
                        applicationName,
                        status,
                        recordState,
                        PageRequest.of(page, limit),
                        principal.getName());
        if (res.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }
}
