package com.example.caselabproject.repositories;

import com.example.caselabproject.models.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UserPageRepository extends PagingAndSortingRepository<User, Long> {


    Page<User> findAllByRoles_nameAndDepartment_nameAndPersonalUserInfo_FirstNameAndPersonalUserInfo_LastNameAndPersonalUserInfo_PatronymicAndPersonalUserInfo_BirthDateAfterAndPersonalUserInfo_BirthDateBeforeAndAuthUserInfo_Email(
            String roleName,
            String departmentName,
            String firstname,
            String lastName,
            String patronymic,
            LocalDate birthDateFrom,
            LocalDate birthDateTo,
            String email,
            Pageable pageable
    );

}
