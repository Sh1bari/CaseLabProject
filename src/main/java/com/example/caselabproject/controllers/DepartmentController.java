package com.example.caselabproject.controllers;

import com.example.caselabproject.models.DTOs.UserDto;
import com.example.caselabproject.models.DTOs.request.DepartmentRequestDto;
import com.example.caselabproject.models.DTOs.response.DepartmentResponseDto;
import com.example.caselabproject.models.DTOs.response.UserGetByIdResponseDto;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.services.DepartmentService;
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

    @PostMapping("/")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<DepartmentResponseDto> createNewDepartment(
            @RequestBody @Valid DepartmentRequestDto departmentRequestDto) {
        DepartmentResponseDto responseDto = departmentService.create(departmentRequestDto);
        return ResponseEntity
                .created(URI.create("/api/department/" + responseDto.getId()))
                .body(responseDto);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> deleteDepartment(
            @PathVariable @Min(value = 1L, message = "Id cant be less than 1") Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
    @PostMapping("/{id}/recover")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> recoverDepartment(
            @PathVariable @Min(value = 1L, message = "Id cant be less than 1") Long id) {
        departmentService.recoverDepartment(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDto> getDepartmentById(
            @PathVariable @Min(value = 1L, message = "Id cant be less than 1") Long id) {
        DepartmentResponseDto responseDto = departmentService.getById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

    @GetMapping("/")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<DepartmentResponseDto>> getAllDepartments(
            @RequestParam(name = "page", defaultValue = "0")@Min(value = 0, message = "Page cant be less than 0") Integer page,
            @RequestParam(name = "limit", defaultValue = "30")@Min(value = 1, message = "Page limit cant be less than 1") Integer limit,
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            @RequestParam(value = "recordState", required = false, defaultValue = "ACTIVE") RecordState recordState) {
        List<DepartmentResponseDto> responseDto = departmentService.getAllDepartmentsPageByPage(PageRequest.of(page, limit), name, recordState);
        if(responseDto.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<List<UserGetByIdResponseDto>> getAllUsersInDepartment(
            @PathVariable @Min(value = 1L, message = "Id cant be less than 1") Long id,
            @RequestParam(value = "recordState", required = false, defaultValue = "ACTIVE") RecordState recordState) {
        List<UserGetByIdResponseDto> responseDto = departmentService.getAllUsersFilteredByDepartment(recordState, id);
        if(responseDto.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }
}
