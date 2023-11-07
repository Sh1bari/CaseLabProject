package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.ApplicationItemPermissionException;
import com.example.caselabproject.exceptions.DepartmentNotFoundException;
import com.example.caselabproject.exceptions.UserExistsException;
import com.example.caselabproject.exceptions.UserNotFoundException;
import com.example.caselabproject.models.DTOs.request.UserCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.UserUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.*;
import com.example.caselabproject.models.entities.ApplicationItem;
import com.example.caselabproject.models.entities.Department;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.ApplicationItemStatus;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.repositories.*;
import com.example.caselabproject.services.RoleService;
import com.example.caselabproject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private final ApplicationPageRepository applicationPageRepository;
    private final ApplicationItemRepository applicationItemRepo;
    private final ApplicationItemPageRepository applicationItemPageRepo;

    @Override
    public UserGetByIdResponseDto getById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return UserGetByIdResponseDto.mapFromEntity(user);
    }

    @Override
    public boolean existById(Long id) {
        boolean exists = userRepository.existsById(id);
        if(!exists){
            throw new UserNotFoundException(id);
        }
        return true;
    }

    @Override
    public UserCreateResponseDto create(UserCreateRequestDto userRequestDto) {
        User user = userRequestDto.mapToEntity();
        user.setRoles(roleService.findRolesByRoleDtoList(userRequestDto.getRoles()));
        user.getAuthUserInfo().setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
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

    @Override
    public List<ApplicationFindResponseDto> findApplicationsByCreatorIdByPage(Long id, Pageable pageable) {
        if (existById(id)) {
            return ApplicationFindResponseDto
                    .mapFromListEntity(applicationPageRepository.findAllByCreatorId_id(id, pageable).toList());
        } else throw new UserNotFoundException(id);
    }

    @Override
    public List<ApplicationItemGetByIdResponseDto> findApplicationItemsByUserIdByPage(
            Long id,
            String applicationName,
            ApplicationItemStatus status,
            RecordState recordState,
            Pageable pageable,
            String username) {
        AtomicBoolean isAdmin = new AtomicBoolean(false);
        User userByUsername = getUserByUsername(username);
        userByUsername.getRoles().forEach(o->{
            if(o.getName().equals("ROLE_ADMIN")){
                isAdmin.set(true);
            }
        });
        User userById = getUserById(id);
        //Can be read only by admins, himself and from the same department
        if(!isAdmin.get() &&
                !userByUsername.getId().equals(id) &&
                !userById.getDepartment().getId().equals(userByUsername.getDepartment().getId())){
            throw new ApplicationItemPermissionException();
        }
        Page<ApplicationItem> applicationItemPage = applicationItemPageRepo
                .findAllByToUser_idAndRecordStateAndApplication_NameContainsIgnoreCase(
                        id,
                        recordState,
                        applicationName,
                        pageable);
        List<ApplicationItem> res;
        if(status!=null){
            res = applicationItemPage.getContent()
                    .stream()
                    .filter(o->o.getStatus().equals(status))
                    .toList();
        }else {
            res = applicationItemPage.getContent();
        }
        return res.stream()
                .map(ApplicationItemGetByIdResponseDto::mapFromEntity)
                .toList();
    }
    private User getUserByUsername(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new UserNotFoundException(username));
        return user;
    }
    private User getUserById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException(id));
        return user;
    }
}
