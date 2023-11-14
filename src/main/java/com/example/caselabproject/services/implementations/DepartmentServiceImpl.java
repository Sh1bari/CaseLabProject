package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.*;
import com.example.caselabproject.models.DTOs.request.DepartmentRequestDto;
import com.example.caselabproject.models.DTOs.response.DepartmentResponseDto;
import com.example.caselabproject.models.DTOs.response.UserGetByIdResponseDto;
import com.example.caselabproject.models.entities.Department;
import com.example.caselabproject.models.entities.DocumentConstructorType;
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
import java.util.Random;
import java.util.stream.Collectors;


@Service
@Validated
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final UserRepository userRepository;


    @Override
    public DepartmentResponseDto create(DepartmentRequestDto requestDto) {

        Department department = requestDto.mapToEntity();
        department.setRecordState(RecordState.ACTIVE);
        department.setUsers(new ArrayList<>());

        Department saveDepartment = saveInternal(department);
        saveDepartment.setSerialKey(generateUniqueSerialKey(saveDepartment));

        return DepartmentResponseDto.mapFromEntity(saveDepartment);

    }

    @Override
    public DepartmentResponseDto updateName(Long departmentId, DepartmentRequestDto requestDto) {

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId));

        department.setName(requestDto.getName());

        return DepartmentResponseDto.mapFromEntity(saveInternal(department));

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

    /**
     * Внутренний метод, позволяющий сохранить Department. Используется
     * для избежания повторов кода.
     */
    private Department saveInternal(Department department) {
        try {
            // при использовании просто save(), мы не сможем обработать ограничение
            // уникальности, поэтому используем saveAndFlush().
            return departmentRepository.saveAndFlush(department);
        } catch (DataIntegrityViolationException ex) {
            throw new DepartmentNameExistsException(department.getName());
        }
    }

    /**
     * Внутренний метод, позволяющий сгенерировать уникальный номер отдела.
     */
    private String generateUniqueSerialKey(Department department) {
        Random random = new Random();
        StringBuilder serialKey = new StringBuilder(3);

        for (int i = 0; i < 3; i++) {
            // Генерируем случайное число от 0 до 25 (для 26 букв алфавита)
            char randomChar = (char) ('a' + random.nextInt(26));
            serialKey.append(randomChar);
        }

        // Добавляем id в конец через дефис
        serialKey.append('-').append(department.getId());

        return serialKey.toString();
    }

}
