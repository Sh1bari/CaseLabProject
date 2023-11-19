package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.UserExistsException;
import com.example.caselabproject.exceptions.UserNotFoundException;
import com.example.caselabproject.models.DTOs.RoleDto;
import com.example.caselabproject.models.DTOs.request.UserCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.UserUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.*;
import com.example.caselabproject.models.entities.PersonalUserInfo;
import com.example.caselabproject.models.entities.Role;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.repositories.*;
import com.example.caselabproject.services.RoleService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.caselabproject.exceptions.*;
import com.example.caselabproject.models.DTOs.request.DocumentConstructorTypePatchRequestDto;
import com.example.caselabproject.models.DTOs.request.DocumentConstructorTypeRequestDto;
import com.example.caselabproject.models.DTOs.request.FieldRequestDto;
import com.example.caselabproject.models.entities.DocumentConstructorType;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.repositories.DocumentConstructorTypeRepository;
import com.example.caselabproject.repositories.DocumentRepository;
import com.example.caselabproject.services.DocumentConstructorTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserPageRepository userPageRepository;
    @Mock
    private DocumentRepository documentRepository;
    @Mock
    private DocumentPageRepository documentPageRepository;
    @Mock
    private RoleService roleService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private ApplicationPageRepository applicationPageRepository;
    @Mock
    private ApplicationItemRepository applicationItemRepository;
    @Mock
    private ApplicationItemPageRepository applicationItemPageRepository;
    @Mock
    private UserRepository userRepository;
    private UserServiceImpl underTest;
    private User user;

    private static UserCreateRequestDto getUserCreateRequestDto() {
        UserCreateRequestDto userCreateRequestDto = new UserCreateRequestDto();
        userCreateRequestDto.setPosition("qwe");
        userCreateRequestDto.setUsername("qwe");
        userCreateRequestDto.setEmail("qwe@qwe.qwe");
        userCreateRequestDto.setFirstName("qwe");
        userCreateRequestDto.setLastName("qwe");
        userCreateRequestDto.setPatronymic("qwe");
        userCreateRequestDto.setBirthDate(LocalDate.of(2003, 6, 19));
        userCreateRequestDto.setUsername("qwe");
        userCreateRequestDto.setPassword("qwe");
        userCreateRequestDto.setRoles(List.of(RoleDto.mapFromEntity(new Role(1, "ROLE_USER"))));
        return userCreateRequestDto;
    }

    private static UserUpdateRequestDto getUserUpdateRequestDto() {
        UserUpdateRequestDto userUpdateRequestDto = new UserUpdateRequestDto();
        userUpdateRequestDto.setPosition("qwe");
        userUpdateRequestDto.setUsername("qwe");
        userUpdateRequestDto.setEmail("qwe@qwe.qwe");
        userUpdateRequestDto.setFirstName("qwe");
        userUpdateRequestDto.setLastName("qwe");
        userUpdateRequestDto.setPatronymic("qwe");
        userUpdateRequestDto.setBirthDate(LocalDate.of(2003, 6, 19));
        userUpdateRequestDto.setUsername("qwe");
        userUpdateRequestDto.setPassword("qwe");
        userUpdateRequestDto.setRoles(List.of(RoleDto.mapFromEntity(new Role(1, "ROLE_USER"))));
        return userUpdateRequestDto;
    }

    @BeforeEach
    void setUp() {
        underTest = new UserServiceImpl(userRepository, userPageRepository, documentRepository, documentPageRepository,
                roleService, passwordEncoder, departmentRepository, applicationPageRepository,
                applicationItemRepository, applicationItemPageRepository);
    }

    @Test
    void getUserByIdTest() {
        //given
        UserGetByIdResponseDto userToGetById = UserGetByIdResponseDto.mapFromEntity(user);
        //when
        UserGetByIdResponseDto userGotById = underTest.getById(user.getId());
        //then
        assertEquals(userToGetById, userGotById);
        assertNotNull(userGotById);
    }

    @Test
    void createUserTest() {
        //given

        UserCreateRequestDto userCreateRequestDto = getUserCreateRequestDto();
        User user1 = userCreateRequestDto.mapToEntity();

        user1.setId(1L);

        user1.setPersonalUserInfo(new PersonalUserInfo(1L, "fn", "ln", "p", LocalDate.now(), user1));
        given(userRepository.saveAndFlush(any())).willReturn(user1);

        underTest.create(userCreateRequestDto);
        verify(userRepository).saveAndFlush(any());
/*        UserCreateResponseDto userToCreate = UserCreateResponseDto.mapFromEntity();
        userToCreate.setRoles(getUserCreateRequestDto().getRoles());
        //when
        UserCreateResponseDto createdUser = underTest.create(getUserCreateRequestDto());
        createdUser.setId(createdUser.getId() - 1);
        //then
        assertEquals(userToCreate, createdUser);
        assertNotNull(createdUser);*/
    }

    @Test
    void updateUserByIdTest() {
        //given
        UserUpdateResponseDto userToUpdate = UserUpdateResponseDto.mapFromEntity(user);
        //when
        UserUpdateResponseDto updatedUser = underTest.updateById(user.getId(), getUserUpdateRequestDto());
        //then
        assertEquals(userToUpdate, updatedUser);
        assertNotNull(updatedUser);
    }

    @Test
    void deleteUserByIdTest() {
        //given
        user.setRecordState(RecordState.DELETED);
        UserDeleteResponseDto userToDelete = UserDeleteResponseDto.mapFromEntity(user);
        //when
        UserDeleteResponseDto deletedUser = underTest.deleteById(user.getId());
        //then
        assertEquals(userToDelete, deletedUser);
        assertNotNull(deletedUser);
    }

    @Test
    void recoverUserByIdTest() {
        //given
        UserRecoverResponseDto userToRecover = UserRecoverResponseDto.mapFromEntity(user);
        //when
        UserRecoverResponseDto recoveredUser = underTest.recoverById(user.getId());
        //then
        assertEquals(userToRecover, recoveredUser);
        assertNotNull(recoveredUser);
    }

    @Test
    void findAllUsersByFiltersByPageTest() {
        //when
        List<UserGetByIdResponseDto> userGetByIdResponseDtoList = underTest.findAllUsersByFiltersByPage(
                "",
                "",
                "",
                "",
                "",
                LocalDate.of(1900, 1, 1),
                LocalDate.of(3000, 1, 1),
                "qwe@qwe.qwe",
                PageRequest.of(0, 1)).stream().toList();
        //then
        verify(userPageRepository).
                findAllByRoles_nameContainsIgnoreCaseAndPersonalUserInfo_FirstNameContainsIgnoreCaseAndPersonalUserInfo_LastNameContainsIgnoreCaseAndPersonalUserInfo_PatronymicContainsIgnoreCaseAndPersonalUserInfo_BirthDateAfterAndPersonalUserInfo_BirthDateBeforeAndAuthUserInfo_EmailContainsIgnoreCase(
                        "",
                        "",
                        "",
                        "",
                        LocalDate.of(1900, 1, 1),
                        LocalDate.of(3000, 1, 1),
                        "qwe@qwe.qwe",
                        PageRequest.of(0, 1));
    }

   /* @Test
    void testUserAndValidationExceptions() {
        // Incorrect username
        assertThrows(UserExistsException.class, () -> {
            createUserIfDoesNotExist();
            underTest.create(getUserCreateRequestDto());
            deleteUserIfExists();
        });
        // Incorrect id
        assertThrows(UserNotFoundException.class, () -> underTest.getById(Long.MAX_VALUE));
        assertThrows(ConstraintViolationException.class, () -> underTest.getById(0L));
        // Incorrect page
        assertThrows(IllegalArgumentException.class, () -> {
            createUserIfDoesNotExist();
            underTest.findAllUsersByFiltersByPage("",
                    "",
                    "",
                    "",
                    "",
                    LocalDate.of(1900, 1, 1),
                    LocalDate.of(3000, 1, 1),
                    "qwe@qwe.qwe",
                    PageRequest.of(0, 0));
            deleteUserIfExists();
        });
        // Incorrect date
        assertThrows(ConstraintViolationException.class, () -> {
            deleteUserIfExists();
            UserCreateRequestDto userCreateRequestDto = getUserCreateRequestDto();
            userCreateRequestDto.setBirthDate(LocalDate.now());
            underTest.create(userCreateRequestDto);
            deleteUserIfExists();
        });
    }*/
}