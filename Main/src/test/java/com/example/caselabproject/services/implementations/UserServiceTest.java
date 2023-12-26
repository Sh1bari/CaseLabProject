package com.example.caselabproject.services.implementations;


import com.example.caselabproject.exceptions.user.UserNotFoundException;
import com.example.caselabproject.models.DTOs.RoleDto;
import com.example.caselabproject.models.DTOs.request.user.UserCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.user.UserUpdateRequestDto;
import com.example.caselabproject.models.entities.Department;
import com.example.caselabproject.models.entities.Role;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.repositories.*;
import com.example.caselabproject.services.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository UserRepository;
    @Mock
    private DocumentRepository DocumentRepository;
    @Mock
    private RoleService roleService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private ApplicationRepository ApplicationRepository;
    @Mock
    private ApplicationItemRepository ApplicationItemRepository;
    @Mock
    private UserRepository userRepository;
    private UserServiceImpl underTest;

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
        userUpdateRequestDto.setRoles(List.of(RoleDto.mapFromEntity(new Role(1, "ROLE_USER"))));
        return userUpdateRequestDto;
    }

    @BeforeEach
    void setUp() {
//        underTest = new UserServiceImpl(userRepository, UserRepository, DocumentRepository, roleService,
//                passwordEncoder, departmentRepository, ApplicationRepository, ApplicationItemRepository);
    }

    @Test
    void getUserByIdTest() {
        UserCreateRequestDto userCreateRequestDto = getUserCreateRequestDto();
        User user1 = userCreateRequestDto.mapToEntity();
        user1.setId(1L);
        user1.setRoles(List.of(new Role()));
        given(userRepository.findById(1L)).willReturn(Optional.of(user1));
        underTest.getById(1L);
        verify(userRepository).findById(any());
    }

    @Test
    void userExistsTest() {
        UserCreateRequestDto userCreateRequestDto = getUserCreateRequestDto();
        User user1 = userCreateRequestDto.mapToEntity();
        user1.setId(1L);
        user1.setRoles(List.of(new Role()));

        given(userRepository.existsById(1L)).willReturn(true);
        underTest.existById(1L);
        verify(userRepository).existsById(any());
    }

    @Test
    void createUserTest() {
        UserCreateRequestDto userCreateRequestDto = getUserCreateRequestDto();
        User user1 = userCreateRequestDto.mapToEntity();
        user1.setId(1L);
        given(userRepository.save(any())).willReturn(user1);
//        underTest.create(userCreateRequestDto);
        verify(userRepository).save(any());
    }

    @Test
    void createUser_willThrowWhenUsernameExists() {
        UserCreateRequestDto userCreateRequestDto = getUserCreateRequestDto();

        given(userRepository.save(any())).willThrow(DataIntegrityViolationException.class);

//        assertThatThrownBy(() -> underTest
//                .create(userCreateRequestDto))
//                .isInstanceOf(UserExistsException.class)
//                .hasMessageContaining("User with username "
//                        + getUserCreateRequestDto().getUsername() + " already exists");
    }

    @Test
    void findUser_willThrowWhenUserNotFound() {
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> underTest
                .getById(1L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User with id " + 1 + " not found.");
    }

    @Test
    void updateUserByIdTest() {
        UserUpdateRequestDto userUpdateRequestDto = getUserUpdateRequestDto();
        User user1 = userUpdateRequestDto.mapToEntity();
        user1.setId(1L);
        user1.setDepartment(new Department());
        given(userRepository.save(any())).willReturn(user1);
        given(userRepository.findById(1L)).willReturn(Optional.of(user1));
        underTest.updateById(1L, userUpdateRequestDto);
        verify(userRepository).save(any());
    }

    @Test
    void deleteUserByIdTest() {
        UserUpdateRequestDto userUpdateRequestDto = getUserUpdateRequestDto();
        User user1 = userUpdateRequestDto.mapToEntity();
        user1.setId(1L);
        user1.setDepartment(new Department());
        user1.setRoles(List.of(new Role()));

        given(userRepository.save(any())).willReturn(user1);
        given(userRepository.findById(1L)).willReturn(Optional.of(user1));

        underTest.deleteById(1L);
        verify(userRepository).save(any());
    }

    @Test
    void recoverUserByIdTest() {
        UserUpdateRequestDto userUpdateRequestDto = getUserUpdateRequestDto();
        User user1 = userUpdateRequestDto.mapToEntity();
        user1.setId(1L);
        user1.setDepartment(new Department());
        user1.setRoles(List.of(new Role()));

        given(userRepository.save(any())).willReturn(user1);
        given(userRepository.findById(1L)).willReturn(Optional.of(user1));

        underTest.recoverById(1L);
        verify(userRepository).save(any());
    }

    @Test
    void findAllUsersByFiltersByPageTest() {
        given(UserRepository
                .findAllByRoles_nameContainsIgnoreCaseAndPersonalUserInfo_FirstNameContainsIgnoreCaseAndPersonalUserInfo_LastNameContainsIgnoreCaseAndPersonalUserInfo_PatronymicContainsIgnoreCaseAndPersonalUserInfo_BirthDateAfterAndPersonalUserInfo_BirthDateBeforeAndAuthUserInfo_EmailContainsIgnoreCase("", "", "", "", LocalDate.of(1900, 1, 1), LocalDate.of(3000, 1, 1), "qwe@qwe.qwe", PageRequest.of(0, 1))).willReturn(new PageImpl<>(List.of(new User())));
        try {
            underTest.findAllUsersByFiltersByPage("", "", "", "", "", LocalDate.of(1900, 1, 1), LocalDate.of(3000, 1, 1), "qwe@qwe.qwe", PageRequest.of(0, 1));
        } catch (NullPointerException ignored) {
        }

        verify(UserRepository)
                .findAllByRoles_nameContainsIgnoreCaseAndPersonalUserInfo_FirstNameContainsIgnoreCaseAndPersonalUserInfo_LastNameContainsIgnoreCaseAndPersonalUserInfo_PatronymicContainsIgnoreCaseAndPersonalUserInfo_BirthDateAfterAndPersonalUserInfo_BirthDateBeforeAndAuthUserInfo_EmailContainsIgnoreCase("", "", "", "", LocalDate.of(1900, 1, 1), LocalDate.of(3000, 1, 1), "qwe@qwe.qwe", PageRequest.of(0, 1));
    }

/*   @Test
    void findAllDocsByUserTest() {
        given(DocumentRepository
                .findAllByCreator_idAndNameContainingIgnoreCaseAndCreationDateAfterAndCreationDateBeforeAndDocumentConstructorType_IdAndRecordState(
                        1L,
                        "",
                        LocalDateTime.of(1900, Month.JANUARY, 1, 1, 1),
                        LocalDateTime.of(3000, Month.JANUARY, 1, 1, 1),
                        1L,
                        RecordState.ACTIVE,
                        PageRequest.of(0, 1)))
                .willReturn(new PageImpl<>(List.of(new Document())));
        underTest.findDocsByFiltersByPage(
                    1L,
                    "",
                    LocalDateTime.of(1900, Month.JANUARY, 1, 1, 1),
                    LocalDateTime.of(3000, Month.JANUARY, 1, 1, 1),
                    1L,
                    RecordState.ACTIVE,
                    PageRequest.of(0, 1));

        verify(DocumentRepository)
                .findAllByCreator_idAndNameContainingIgnoreCaseAndCreationDateAfterAndCreationDateBeforeAndDocumentConstructorType_IdAndRecordState(
                        1L,
                        "",
                        LocalDateTime.of(1900, Month.JANUARY, 1, 1, 1),
                        LocalDateTime.of(3000, Month.JANUARY, 1, 1, 1),
                        1L,
                        RecordState.ACTIVE,
                        PageRequest.of(0, 1));
    }*/
}