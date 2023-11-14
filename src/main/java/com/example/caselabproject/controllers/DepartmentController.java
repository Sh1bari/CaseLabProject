package com.example.caselabproject.controllers;

import com.example.caselabproject.exceptions.AppError;
import com.example.caselabproject.models.DTOs.UserDto;
import com.example.caselabproject.models.DTOs.request.DepartmentRequestDto;
import com.example.caselabproject.models.DTOs.response.DepartmentResponseDto;
import com.example.caselabproject.models.DTOs.response.UserGetByIdResponseDto;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
                                    schema = @Schema(implementation = DepartmentResponseDto.class))
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
    public ResponseEntity<DepartmentResponseDto> createNewDepartment(
            @RequestBody @Valid DepartmentRequestDto departmentRequestDto) {
        DepartmentResponseDto responseDto = departmentService.create(departmentRequestDto);
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
                                    schema = @Schema(implementation = DepartmentResponseDto.class))
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
    public ResponseEntity<DepartmentResponseDto> updateDepartmentNameById(
            @PathVariable @Min(value = 1L, message = "Id cant be less than 1") Long id,
            @RequestBody @Valid DepartmentRequestDto departmentRequestDto) {
        DepartmentResponseDto responseDto = departmentService.updateName(id, departmentRequestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

    @Operation(summary = "Get a department by ID", description = "Retrieves details of a specific department by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Department found and returned successfully",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DepartmentResponseDto.class))
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
    public ResponseEntity<DepartmentResponseDto> getDepartmentById(
            @PathVariable @Min(value = 1L, message = "Id cant be less than 1") Long id) {
        DepartmentResponseDto responseDto = departmentService.getById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }


    @Operation(summary = "Get all departments", description = "Retrieves a list of departments with pagination and optional filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of departments retrieved successfully",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DepartmentResponseDto.class))
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
    public ResponseEntity<List<DepartmentResponseDto>> getAllDepartments(
            @RequestParam(name = "page", defaultValue = "0") @Min(value = 0, message = "Page cant be less than 0") Integer page,
            @RequestParam(name = "limit", defaultValue = "30") @Min(value = 1, message = "Page limit cant be less than 1") Integer limit,
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            @RequestParam(value = "recordState", required = false, defaultValue = "ACTIVE") RecordState recordState) {
        List<DepartmentResponseDto> responseDto = departmentService.getAllDepartmentsPageByPage(PageRequest.of(page, limit), name, recordState);
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
}
