package com.example.caselabproject;

import com.example.caselabproject.exceptions.UserExistsException;
import com.example.caselabproject.exceptions.UserNotFoundException;
import com.example.caselabproject.models.DTOs.RoleDto;
import com.example.caselabproject.models.DTOs.request.UserCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.UserUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.*;
import com.example.caselabproject.models.entities.Role;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.repositories.UserRepository;
import com.example.caselabproject.services.implementations.UserServiceImpl;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;
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
    void createUserIfDoesNotExist() {
        if (userRepository.findByUsername(getUserCreateRequestDto().getUsername()).isEmpty()) {
            userService.create(getUserCreateRequestDto());
        }
        user = userRepository.findByUsername(getUserCreateRequestDto().getUsername()).get();
    }

    @AfterEach
    void deleteUserIfExists() {
        if (userRepository.findByUsername(getUserCreateRequestDto().getUsername()).isPresent()) {
            userRepository.deleteById(userRepository.findByUsername(getUserCreateRequestDto().getUsername()).get().getId());
        }
    }

    @Test
    void getUserByIdTest() {
        //given
        UserGetByIdResponseDto userToGetById = UserGetByIdResponseDto.mapFromEntity(user);
        //when
        UserGetByIdResponseDto userGotById = userService.getById(user.getId());
        //then
        assertEquals(userToGetById, userGotById);
        assertNotNull(userGotById);
    }

    @Test
    void createUserTest() {
        deleteUserIfExists();
        //given
        UserCreateResponseDto userToCreate = UserCreateResponseDto.mapFromEntity(user);
        userToCreate.setRoles(getUserCreateRequestDto().getRoles());
        //when
        UserCreateResponseDto createdUser = userService.create(getUserCreateRequestDto());
        createdUser.setId(createdUser.getId() - 1);
        //then
        assertEquals(userToCreate, createdUser);
        assertNotNull(createdUser);
    }

    @Test
    void updateUserByIdTest() {
        //given
        UserUpdateResponseDto userToUpdate = UserUpdateResponseDto.mapFromEntity(user);
        //when
        UserUpdateResponseDto updatedUser = userService.updateById(user.getId(), getUserUpdateRequestDto());
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
        UserDeleteResponseDto deletedUser = userService.deleteById(user.getId());
        //then
        assertEquals(userToDelete, deletedUser);
        assertNotNull(deletedUser);
    }

    @Test
    void recoverUserByIdTest() {
        //given
        UserRecoverResponseDto userToRecover = UserRecoverResponseDto.mapFromEntity(user);
        //when
        UserRecoverResponseDto recoveredUser = userService.recoverById(user.getId());
        //then
        assertEquals(userToRecover, recoveredUser);
        assertNotNull(recoveredUser);
    }

    @Test
    void findAllUsersByFiltersByPageTest() {
        //when
        List<UserGetByIdResponseDto> userGetByIdResponseDtoList = userService.findAllUsersByFiltersByPage("",
                "",
                "",
                "",
                "",
                LocalDate.of(1900, 1, 1),
                LocalDate.of(3000, 1, 1),
                "qwe@qwe.qwe",
                PageRequest.of(0, 1));
        //then
        assertNotNull(userGetByIdResponseDtoList);
        assertFalse(userGetByIdResponseDtoList.isEmpty());
        assertEquals(1, userGetByIdResponseDtoList.size());
    }

    @Test
    void testUserAndValidationExceptions() {
        // Incorrect username
        assertThrows(UserExistsException.class, () -> {
            createUserIfDoesNotExist();
            userService.create(getUserCreateRequestDto());
            deleteUserIfExists();
        });
        // Incorrect id
        assertThrows(UserNotFoundException.class, () -> userService.getById(Long.MAX_VALUE));
        assertThrows(ConstraintViolationException.class, () -> userService.getById(0L));
        // Incorrect page
        assertThrows(IllegalArgumentException.class, () -> {
            createUserIfDoesNotExist();
            userService.findAllUsersByFiltersByPage("",
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
            userService.create(userCreateRequestDto);
            deleteUserIfExists();
        });
    }
}