package com.example.caselabproject.services;

import com.example.caselabproject.models.DTOs.request.DepartmentRequestDto;
import com.example.caselabproject.models.DTOs.response.DepartmentResponseDto;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.RecordState;


import javax.validation.Valid;
import java.util.List;


public interface DepartmentService {
    DepartmentResponseDto create(@Valid DepartmentRequestDto departmentRequestDto);

    DepartmentResponseDto updateRecordState(Long departmentId);

    DepartmentResponseDto getById(Long departmentId);

    List<DepartmentResponseDto> getAllDepartmentsPageByPage(Integer page);


    List<User> GetAllUsersFilteredByDepartment(RecordState recordState, Long departmentId);

}
