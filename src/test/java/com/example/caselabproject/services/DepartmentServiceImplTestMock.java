package com.example.caselabproject.services;

import com.example.caselabproject.models.DTOs.request.DepartmentRequestDto;
import com.example.caselabproject.models.DTOs.response.DepartmentResponseDto;
import com.example.caselabproject.models.DTOs.response.UserGetByIdResponseDto;
import com.example.caselabproject.models.entities.AuthUserInfo;
import com.example.caselabproject.models.entities.Department;
import com.example.caselabproject.models.entities.PersonalUserInfo;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.repositories.ApplicationItemPageRepository;
import com.example.caselabproject.repositories.DepartmentRepository;
import com.example.caselabproject.repositories.UserRepository;
import com.example.caselabproject.services.implementations.DepartmentServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class DepartmentServiceImplTestMock {


    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private UserRepository userRepository;


    private DepartmentServiceImpl departmentService;
    @Mock
    private ApplicationItemPageRepository applicationItemPageRepo;


    @BeforeEach
    void setUp() {
        departmentService = new DepartmentServiceImpl(departmentRepository, userRepository, applicationItemPageRepo);
    }


    @Test
    void create_Test() {
        DepartmentRequestDto requestDto = new DepartmentRequestDto();
        requestDto.setName("Name");

        Department department = requestDto.mapToEntity();
        department.setRecordState(RecordState.ACTIVE);
        department.setUsers(new ArrayList<>());
        department.setId(1L);

        DepartmentResponseDto responseDto = DepartmentResponseDto.mapFromEntity(department);

        given(departmentRepository.save(any(Department.class))).willReturn(department);

        DepartmentResponseDto responseDto1 = departmentService.create(requestDto);

        Assertions.assertEquals(responseDto, responseDto1);

    }

    @Test
    void deleteDepartment_Test() {

        Department department = new Department();
        department.setId(1L);
        department.setRecordState(RecordState.ACTIVE);

        given(departmentRepository.findById(1L)).willReturn(Optional.of(department));
        given(departmentRepository.save(any(Department.class))).willReturn(department);

        boolean result = departmentService.deleteDepartment(1L);
        verify(departmentRepository).findById(1L);

        verify(departmentRepository).save(department);

        assertTrue(result);

        assertEquals(RecordState.DELETED, department.getRecordState());
    }

    @Test
    void recoverDepartment_Test() {

        Department department = new Department();
        department.setId(1L);
        department.setRecordState(RecordState.DELETED);
        department.setUsers(new ArrayList<>());

        given(departmentRepository.findById(1L)).willReturn(Optional.of(department));
        given(departmentRepository.save(any(Department.class))).willReturn(department);

        departmentService.recoverDepartment(1L);

        verify(departmentRepository).findById(1L);

        verify(departmentRepository).save(department);

        assertEquals(RecordState.ACTIVE, department.getRecordState());
    }

    @Test
    void getById_Test() {
        Department department = new Department();
        department.setId(1L);
        department.setRecordState(RecordState.ACTIVE);
        department.setUsers(new ArrayList<>());


        DepartmentResponseDto responseDto = DepartmentResponseDto.mapFromEntity(department);

        given(departmentRepository.findById(1L)).willReturn(Optional.of(department));


        DepartmentResponseDto responseDto1 = departmentService.getById(1L);

        verify(departmentRepository).findById(1L);

        assertEquals(responseDto, responseDto1);

    }

    @Test
    void getAllDepartmentsPageByPage_Test() {

        Department department1 = new Department();
        department1.setId(1L);
        department1.setName("Department 1");
        department1.setRecordState(RecordState.ACTIVE);
        department1.setUsers(new ArrayList<>());

        Department department2 = new Department();
        department2.setId(2L);
        department2.setName("Department 2");
        department2.setRecordState(RecordState.ACTIVE);
        department2.setUsers(new ArrayList<>());

        List<Department> departmentList = List.of(department1, department2);
        Page<Department> departmentPage = new PageImpl<>(departmentList);


        given(departmentRepository.findDepartmentsByNameContainingAndRecordState(
                anyString(), any(Pageable.class), any(RecordState.class))).willReturn(departmentPage);


        Page<DepartmentResponseDto> responseDtoList =
                departmentService.getAllDepartmentsPageByPage(Pageable.unpaged(), "Department", RecordState.ACTIVE);


        verify(departmentRepository).findDepartmentsByNameContainingAndRecordState(
                "Department", Pageable.unpaged(), RecordState.ACTIVE);

        assertNotNull(responseDtoList);
        assertEquals(2, responseDtoList.getContent().size());

        DepartmentResponseDto responseDto1 = responseDtoList.getContent().get(0);
        assertEquals(1L, responseDto1.getId());
        assertEquals("Department 1", responseDto1.getName());


        DepartmentResponseDto responseDto2 = responseDtoList.getContent().get(1);
        assertEquals(2L, responseDto2.getId());
        assertEquals("Department 2", responseDto2.getName());


    }

    @Test
    void getAllUsersFilteredByDepartment_Test() {

        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("User 1");
        user1.setRoles(new ArrayList<>());
        user1.setPersonalUserInfo(PersonalUserInfo.builder().build());
        user1.setRecordState(RecordState.ACTIVE);
        user1.setAuthUserInfo(AuthUserInfo.builder().build());

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("User 2");
        user2.setRoles(new ArrayList<>());
        user2.setPersonalUserInfo(PersonalUserInfo.builder().build());
        user2.setRecordState(RecordState.ACTIVE);
        user2.setAuthUserInfo(AuthUserInfo.builder().build());


        List<User> usersList = List.of(user1, user2);
        Page<User> userList = new PageImpl<>(usersList);

        given(userRepository.findByRecordStateAndDepartment_Id(
                Pageable.unpaged(), RecordState.ACTIVE, 1L)).willReturn(userList);


        Page<UserGetByIdResponseDto> responseDtoList =
                departmentService.getAllUsersFilteredByDepartment(Pageable.unpaged(), RecordState.ACTIVE, 1L);

        verify(userRepository).findByRecordStateAndDepartment_Id(
                Pageable.unpaged(), RecordState.ACTIVE, 1L);


        assertNotNull(responseDtoList);
        assertEquals(2, responseDtoList.getContent().size());

        UserGetByIdResponseDto responseDto1 = responseDtoList.getContent().get(0);

        assertEquals(1L, responseDto1.getId());
        assertEquals("User 1", responseDto1.getUsername());

        UserGetByIdResponseDto responseDto2 = responseDtoList.getContent().get(1);
        assertEquals(2L, responseDto2.getId());
        assertEquals("User 2", responseDto2.getUsername());

    }

}
