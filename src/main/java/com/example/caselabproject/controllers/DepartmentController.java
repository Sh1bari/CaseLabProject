package com.example.caselabproject.controllers;

import com.example.caselabproject.models.DTOs.UserDto;
import com.example.caselabproject.models.DTOs.request.DepartmentRequestDto;
import com.example.caselabproject.models.DTOs.response.DepartmentResponseDto;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.services.DepartmentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
@CrossOrigin
@RequestMapping("/department")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping("/")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<DepartmentResponseDto> createNewDepartment(@RequestBody DepartmentRequestDto departmentRequestDto) {
        DepartmentResponseDto responseDto = departmentService.create(departmentRequestDto);
        return ResponseEntity
                .created(URI.create("/api/department/" + responseDto.getId()))
                .body(responseDto);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> updateRecordStateOfDepartment(@PathVariable Long id) {
        departmentService.updateRecordState(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDto> getDepartmentById(@PathVariable Long id) {
        DepartmentResponseDto responseDto = departmentService.getById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

    @GetMapping("/")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<DepartmentResponseDto>> getAllDepartments(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "name", required = false) String name) {
        List<DepartmentResponseDto> responseDto = departmentService.getAllDepartmentsPageByPage(page, name);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<List<UserDto>> getAllUsersInDepartment(@PathVariable Long id, @RequestParam RecordState recordState) {
        List<UserDto> responseDto = departmentService.GetAllUsersFilteredByDepartment(recordState, id).stream().map(UserDto::mapFromEntity).toList();

        return ResponseEntity
                .created(URI.create("/api/department/"))
                .body(responseDto);
    }
}
