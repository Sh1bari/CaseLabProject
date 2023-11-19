package com.example.caselabproject.repositories;

import com.example.caselabproject.exceptions.UserNotFoundException;
import com.example.caselabproject.models.entities.*;
import com.example.caselabproject.models.enums.RecordState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.caselabproject.models.entities.DocumentConstructorType;
import com.example.caselabproject.models.enums.RecordState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AuthUserInfoRepository authUserInfoRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findByUsername() {
        String username = "username";
        roleRepository.save(new Role(1, "USER_ADMIN"));
        User user = new User(
                1L,
                username,
                RecordState.ACTIVE,
                new AuthUserInfo(),
                new ArrayList<Role>(List.of(new Role(1, "USER_ADMIN"))),
                new Department(),
                "position",
                new ArrayList<>(List.of(new Document())),
                new PersonalUserInfo(),
                new ArrayList<>(List.of(new Application())),
                new ArrayList<>(List.of(new ApplicationItem()))
        );
        user.setAuthUserInfo(new AuthUserInfo(1L, "qwe", "qwe@qwe.qwe", user));
        user.setDepartment(new Department(1L, "name", List.of(user), List.of(new ApplicationItem()), RecordState.ACTIVE));
        departmentRepository.save(new Department(1L, "name", List.of(user), List.of(new ApplicationItem()), RecordState.ACTIVE));
        underTest.save(user);
        authUserInfoRepository.save(new AuthUserInfo(1L, "qwe", "qwe@qwe.qwe", user));


        User foundUser = underTest.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    void existsByUsernameAndDocuments_id() {
    }

    @Test
    void findByRecordStateAndDepartment_Id() {
    }

    @Test
    void findByIdAndDepartment_id() {
    }
}