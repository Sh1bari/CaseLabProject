package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.*;
import com.example.caselabproject.models.DTOs.request.DepartmentRequestDto;
import com.example.caselabproject.models.DTOs.response.DepartmentResponseDto;
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

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Validated
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final UserRepository userRepository;
    private final Integer limit = 10;


    @Override
    public DepartmentResponseDto create(@Valid DepartmentRequestDto requestDto) {
        try {
            Department department = requestDto.mapToEntity();
            department.setRecordState(RecordState.ACTIVE);
            departmentRepository.save(department);

            return DepartmentResponseDto.mapFromEntity(department);

        } catch (DataIntegrityViolationException ex) {
            throw new DepartmentNameExistsException(422, "Department " + requestDto.getName() + " already exists.");
        } catch (Exception e) {
            throw new DepartmentCreateException(500, "Can not create department!");
        }

    }

    @Override
    public void deleteDepartment(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId));

        if (department.getRecordState().equals(RecordState.DELETED)) {
            throw new DepartmentStatusException(409, "Department " + departmentId + " already has the status DELETED");
        }

        department.setRecordState(RecordState.DELETED);
        departmentRepository.save(department);
    }


    @Override
    public DepartmentResponseDto getById(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId));

        return DepartmentResponseDto.mapFromEntity(department);
    }


    @Override
    public List<DepartmentResponseDto> getAllDepartmentsPageByPage(Integer page) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Department> departments = departmentRepository.findAll(pageable);
        return departments.getContent().stream()
                .map(DepartmentResponseDto::mapFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> GetAllUsersFilteredByDepartment(RecordState recordState, Long departmentId) {
        List<User> users = userRepository.findByRecordStateAndDepartment_Id(recordState, departmentId)
                .orElseThrow(() -> new CustomUserException(404, "No users found with the given criteria"));

        if (users.isEmpty()) {
            throw new CustomUserException(404, "No users found for the given department ID: " + departmentId);
        }

        return users;
    }


}
