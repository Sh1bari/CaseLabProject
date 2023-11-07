package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.*;
import com.example.caselabproject.models.DTOs.request.DepartmentRequestDto;
import com.example.caselabproject.models.DTOs.response.DepartmentResponseDto;
import com.example.caselabproject.models.DTOs.response.UserGetByIdResponseDto;
import com.example.caselabproject.models.entities.Department;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.repositories.DepartmentRepository;
import com.example.caselabproject.repositories.UserRepository;
import com.example.caselabproject.services.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Validated
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final UserRepository userRepository;


    @Override
    public DepartmentResponseDto create(DepartmentRequestDto requestDto) {
        try {
            Department department = requestDto.mapToEntity();
            department.setRecordState(RecordState.ACTIVE);
            department.setUsers(new ArrayList<>());
            departmentRepository.save(department);

            return DepartmentResponseDto.mapFromEntity(department);

        } catch (DataIntegrityViolationException ex) {
            throw new DepartmentNameExistsException(requestDto.getName());
        } catch (Exception e) {
            throw new DepartmentCreateException();
        }

    }

    @Override
    public boolean deleteDepartment(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId));

        if (department.getRecordState().equals(RecordState.DELETED)) {
            throw new DepartmentStatusException(departmentId, RecordState.DELETED);
        }

        department.setRecordState(RecordState.DELETED);
        departmentRepository.save(department);
        return true;
    }

    @Override
    public DepartmentResponseDto recoverDepartment(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId));

        if (department.getRecordState().equals(RecordState.ACTIVE)) {
            throw new DepartmentStatusException(departmentId, RecordState.ACTIVE);
        }

        department.setRecordState(RecordState.ACTIVE);
        departmentRepository.save(department);
        return DepartmentResponseDto.mapFromEntity(department);
    }


    @Override
    public DepartmentResponseDto getById(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId));

        return DepartmentResponseDto.mapFromEntity(department);
    }


    @Override
    public List<DepartmentResponseDto> getAllDepartmentsPageByPage(Pageable pageable, String name, RecordState recordState) {

        Page<Department> departments =
                departmentRepository.findDepartmentsByNameContainingAndRecordState(name, pageable, recordState);

        return departments.getContent().stream()
                .map(DepartmentResponseDto::mapFromEntity)
                .collect(Collectors.toList());
    }


    @Override
    public List<UserGetByIdResponseDto> getAllUsersFilteredByDepartment(RecordState recordState, Long departmentId) {
        List<UserGetByIdResponseDto> users = userRepository.findByRecordStateAndDepartment_Id(recordState, departmentId).stream().map(UserGetByIdResponseDto::mapFromEntity).toList();
        return users;
    }


}
