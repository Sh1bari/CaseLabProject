package com.example.caselabproject.repositories;

import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.RecordState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface UserPageRepository extends PagingAndSortingRepository<User, Long> {

    Page<User> findAllByRoles_nameContainsIgnoreCaseAndPersonalUserInfo_FirstNameContainsIgnoreCaseAndPersonalUserInfo_LastNameContainsIgnoreCaseAndPersonalUserInfo_PatronymicContainsIgnoreCaseAndPersonalUserInfo_BirthDateAfterAndPersonalUserInfo_BirthDateBeforeAndAuthUserInfo_EmailContainsIgnoreCaseAndRecorState(
            String roleName,
            String firstname,
            String lastName,
            String patronymic,
            LocalDate birthDateFrom,
            LocalDate birthDateTo,
            String email,
            Pageable pageable,
            RecordState recordState
    );
}
