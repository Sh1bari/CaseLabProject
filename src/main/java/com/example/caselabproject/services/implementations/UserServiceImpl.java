package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.DepartmentNotFoundException;
import com.example.caselabproject.exceptions.UserExistsException;
import com.example.caselabproject.exceptions.UserNotFoundException;
import com.example.caselabproject.models.DTOs.request.UserCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.UserUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.*;
import com.example.caselabproject.models.entities.Department;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.repositories.*;
import com.example.caselabproject.services.RoleService;
import com.example.caselabproject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserPageRepository userPageRepository;
    private final DocumentRepository documentRepository;
    private final DocumentPageRepository documentPageRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final DepartmentRepository departmentRepo;

    @Override
    public UserGetByIdResponseDto getById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return UserGetByIdResponseDto.mapFromEntity(user);
    }

    @Override
    public boolean existById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public UserCreateResponseDto create(UserCreateRequestDto userCreateRequestDto) {
        User user = userCreateRequestDto.mapToEntity();
        user.setRoles(roleService.findRolesByRoleDtoList(userCreateRequestDto.getRoles()));
        user.getAuthUserInfo().setPassword(passwordEncoder.encode(userCreateRequestDto.getPassword()));
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new UserExistsException(user.getUsername());
        }
        return UserCreateResponseDto.mapFromEntity(user);
    }

    @Override
    @Transactional
    public UserUpdateResponseDto updateById(Long id, UserUpdateRequestDto userUpdateRequestDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        if (userUpdateRequestDto.getPosition() != null) {
            user.setPosition(userUpdateRequestDto.getPosition());
        }
        if (userUpdateRequestDto.getUsername() != null) {
            user.setUsername(userUpdateRequestDto.getUsername());
        }
        if (userUpdateRequestDto.getDepartmentId() != null) {
            user.setDepartment(departmentRepo.findById(userUpdateRequestDto.getDepartmentId())
                    .orElseThrow(() -> new DepartmentNotFoundException(userUpdateRequestDto.getDepartmentId())));
        }
        if (userUpdateRequestDto.getRoles() != null) {
            user.setRoles(roleService.findRolesByRoleDtoList(userUpdateRequestDto.getRoles()));
        }
        if (userUpdateRequestDto.getPassword() != null) {
            user.getAuthUserInfo().setPassword(userUpdateRequestDto.getPassword());
        }
        if (userUpdateRequestDto.getEmail() != null) {
            user.getAuthUserInfo().setEmail(userUpdateRequestDto.getEmail());
        }
        if (userUpdateRequestDto.getUsername() != null) {
            user.setUsername(userUpdateRequestDto.getUsername());
        }
        if (userUpdateRequestDto.getFirstName() != null) {
            user.getPersonalUserInfo().setFirstName(userUpdateRequestDto.getFirstName());
        }
        if (userUpdateRequestDto.getPatronymic() != null) {
            user.getPersonalUserInfo().setPatronymic(userUpdateRequestDto.getPatronymic());
        }
        if (userUpdateRequestDto.getLastName() != null) {
            user.getPersonalUserInfo().setLastName(userUpdateRequestDto.getLastName());
        }
        if (userUpdateRequestDto.getBirthDate() != null) {
            user.getPersonalUserInfo().setBirthDate(userUpdateRequestDto.getBirthDate());
        }
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new UserExistsException(userUpdateRequestDto.getUsername());
        }
        return UserUpdateResponseDto.mapFromEntity(user);
    }

    @Override
    @Transactional
    public UserDeleteResponseDto deleteById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        user.setRecordState(RecordState.DELETED);
        userRepository.save(user);
        return UserDeleteResponseDto.mapFromEntity(user);
    }

    @Override
    public UserRecoverResponseDto recoverById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        user.setRecordState(RecordState.ACTIVE);
        userRepository.save(user);
        return UserRecoverResponseDto.mapFromEntity(user);
    }

    @Override
    @Transactional
    public List<DocumentCreateResponseDto> findDocsByFiltersByPage(Long creatorId,
                                                                   String name,
                                                                   LocalDateTime creationDateFrom,
                                                                   LocalDateTime creationDateTo,
                                                                   Long documentConstructorTypeId,
                                                                   RecordState recordState,
                                                                   Pageable pageable) {
        if (existById(creatorId)) {
            List<DocumentCreateResponseDto> documentCreateResponseDtoList = DocumentCreateResponseDto
                    .mapFromListOfEntities(documentPageRepository
                            .findAllByCreator_idAndNameContainingIgnoreCaseAndCreationDateAfterAndCreationDateBeforeAndDocumentConstructorType_IdAndRecordState(
                                    creatorId,
                                    name,
                                    creationDateFrom,
                                    creationDateTo,
                                    documentConstructorTypeId,
                                    recordState,
                                    pageable).toList());
            return documentCreateResponseDtoList;
        } else {
            throw new UserNotFoundException(creatorId);
        }
    }

    @Override
    public List<UserGetByIdResponseDto> findAllUsersByFiltersByPage(String roleName,
                                                                    String departmentName,
                                                                    String firstName,
                                                                    String lastName,
                                                                    String patronymic,
                                                                    LocalDate birthDateFrom,
                                                                    LocalDate birthDateTo,
                                                                    String email,
                                                                    Pageable pageable) {
        List<UserGetByIdResponseDto> userCreateResponseDtoList = UserGetByIdResponseDto.mapFromEntities(
                userPageRepository
                        .findAllByRoles_nameContainsIgnoreCaseAndPersonalUserInfo_FirstNameContainsIgnoreCaseAndPersonalUserInfo_LastNameContainsIgnoreCaseAndPersonalUserInfo_PatronymicContainsIgnoreCaseAndPersonalUserInfo_BirthDateAfterAndPersonalUserInfo_BirthDateBeforeAndAuthUserInfo_EmailContainsIgnoreCase(
                                roleName,
                                firstName,
                                lastName,
                                patronymic,
                                birthDateFrom,
                                birthDateTo,
                                email,
                                pageable).toList());
        if (!departmentName.isEmpty()) {
            Department department = departmentRepo
                    .findByName(departmentName)
                    .orElseThrow(() -> new DepartmentNotFoundException(departmentName));
            userCreateResponseDtoList = userCreateResponseDtoList
                    .stream()
                    .filter(o -> o.getDepartmentId().equals(department.getId()))
                    .toList();
        }
        return userCreateResponseDtoList;
    }
}
