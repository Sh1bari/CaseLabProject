package com.example.caselabproject.repositories;

import com.example.caselabproject.models.entities.Organization;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.RecordState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByIsDirectorAndDepartment_Id(Boolean isDirector, Long id);

    List<User> findByIsDirectorAndRecordStateAndDepartment_Id(Boolean isDirector, RecordState recordState, Long departmentId);

    boolean existsByUsernameAndDocuments_id(String username, Long documentId);

    Page<User> findByRecordStateAndDepartment_Id(Pageable pageable, RecordState recordState, Long departmentId);

    Page<User> findByRecordStateAndDepartment_IdAndOrganization(RecordState recordState, Pageable pageable, Long departmentId, Organization organization);

    @Query("SELECT u FROM User u WHERE u.department.id = :departmentId AND u.isDirector = true")
    Optional<User> findByDepartmentIdAndIsDirectorTrue(@Param("departmentId") Long departmentId);

    Optional<User> findByIdAndDepartment_id(Long userId, Long departmentId);
}