package com.example.caselabproject.services.implementations;

import com.example.caselabproject.models.DTOs.request.department.DepartmentCreateRequestDto;
import com.example.caselabproject.models.DTOs.response.department.DepartmentCreateResponseDto;
import com.example.caselabproject.models.DTOs.response.department.DepartmentGetAllResponseDto;
import com.example.caselabproject.models.DTOs.response.department.DepartmentGetByIdResponseDto;
import com.example.caselabproject.models.DTOs.response.user.UserGetByIdResponseDto;
import com.example.caselabproject.models.entities.*;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class DepartmentServiceImplTestMock {


    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRepository userRepo;


    private DepartmentServiceImpl departmentService;

    @Mock
    private ApplicationItemPageRepository applicationItemPageRepo;


    @BeforeEach
    void setUp() {
        departmentService = new DepartmentServiceImpl(departmentRepository, userRepository, applicationItemPageRepo, userRepo);
    }


    @Test
    void create_Test() {
        DepartmentCreateRequestDto requestDto = new DepartmentCreateRequestDto();
        requestDto.setName("Name");

        Department department = requestDto.mapToEntity();
        department.setId(1L);
        User user = new User();
        user.setUsername("User");
        user.setId(1L);

        given(departmentRepository.save(any(Department.class))).willReturn(department);

        given(userRepo.findByUsername(any())).willReturn(Optional.of(user));

        DepartmentCreateResponseDto responseDto = DepartmentCreateResponseDto.mapFromEntity(department);

        DepartmentCreateResponseDto responseDto1 = departmentService.create(requestDto, user.getUsername());

        responseDto.setSerialKey(responseDto1.getSerialKey());


        Assertions.assertEquals(responseDto, responseDto1);

    }

    @Test
    void deleteDepartment_Test() {

        Department department = new Department();
        department.setId(1L);
        department.setRecordState(RecordState.ACTIVE);

        given(departmentRepository.findById(1L)).willReturn(Optional.of(department));
        given(departmentRepository.save(any(Department.class))).willReturn(department);

        departmentService.deleteDepartment(1L);
        verify(departmentRepository).findById(1L);

        verify(departmentRepository).save(department);


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
        department.setChildDepartments(new ArrayList<>());


        DepartmentGetByIdResponseDto responseDto = DepartmentGetByIdResponseDto.mapFromEntity(department);

        given(departmentRepository.findById(1L)).willReturn(Optional.of(department));


        DepartmentGetByIdResponseDto responseDto1 = departmentService.getById(1L);

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

        User user = new User();
        user.setUsername("username");
        user.setCreatedOrganization(new Organization());

        List<Department> departmentList = List.of(department1, department2);
        Page<Department> departmentPage = new PageImpl<>(departmentList);

        String serialKey = "dwd-1";

        given(userRepo.findByUsername(user.getUsername())).willReturn(Optional.of(user));

        given(departmentRepository.findDepartmentsByNameContainingAndRecordStateAndSerialKeyAndOrganization(
                anyString(), any(Pageable.class), any(RecordState.class), any(String.class), any(Organization.class))).willReturn(departmentPage);


        Page<DepartmentGetAllResponseDto> responseDtoList =
                departmentService.getAllDepartmentsPageByPage(Pageable.unpaged(), "Department", RecordState.ACTIVE, serialKey, "username");


        verify(departmentRepository).findDepartmentsByNameContainingAndRecordStateAndSerialKeyAndOrganization(
                anyString(), any(Pageable.class), any(RecordState.class), any(String.class), any(Organization.class));

        assertNotNull(responseDtoList);
        assertEquals(2, responseDtoList.getContent().size());

        DepartmentGetAllResponseDto responseDto1 = responseDtoList.getContent().get(0);
        assertEquals(1L, responseDto1.getId());
        assertEquals("Department 1", responseDto1.getName());


        DepartmentGetAllResponseDto responseDto2 = responseDtoList.getContent().get(1);
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


        List<User> userList = List.of(user1, user2);
        Page<User> userPage = new PageImpl<>(userList);

        User user = new User();
        user.setUsername("username");
        user.setCreatedOrganization(new Organization());

        given(userRepo.findByUsername(user.getUsername())).willReturn(Optional.of(user));


        given(userRepository.findByRecordStateAndDepartment_IdAndOrganization(
                eq(RecordState.ACTIVE), any(Pageable.class), any(Long.class), any(Organization.class))).willReturn(userPage);


        Page<UserGetByIdResponseDto> responseDtoList =
                departmentService.getAllUsersFilteredByDepartment(RecordState.ACTIVE, 1L, Pageable.unpaged(),"username");

        verify(userRepository).findByRecordStateAndDepartment_IdAndOrganization(
                eq(RecordState.ACTIVE), any(Pageable.class), eq(1L), any(Organization.class));


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
