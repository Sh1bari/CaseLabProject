package com.example.caselabproject.services;

import com.example.caselabproject.models.DTOs.request.DepartmentRequestDto;
import com.example.caselabproject.models.DTOs.response.DepartmentResponseDto;
import com.example.caselabproject.models.DTOs.response.UserGetByIdResponseDto;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.RecordState;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface DepartmentService {
    @Transactional
    DepartmentResponseDto create(@Valid DepartmentRequestDto departmentRequestDto);
    @Transactional
    boolean deleteDepartment(@Min(value = 1L, message = "Id cant be less than 1")Long departmentId);
    @Transactional
    DepartmentResponseDto recoverDepartment(@Min(value = 1L, message = "Id cant be less than 1")Long departmentId);
    @Transactional
    DepartmentResponseDto getById(@Min(value = 1L, message = "Id cant be less than 1")Long departmentId);
    @Transactional
    List<DepartmentResponseDto> getAllDepartmentsPageByPage(Pageable pageable, String name, RecordState recordState);

    @Transactional
    List<UserGetByIdResponseDto> getAllUsersFilteredByDepartment(RecordState recordState,
                                                                 @Min(value = 1L, message = "Id cant be less than 1") Long departmentId);

}
