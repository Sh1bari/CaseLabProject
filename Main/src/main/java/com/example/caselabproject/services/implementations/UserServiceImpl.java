package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.applicationItem.ApplicationItemPermissionException;
import com.example.caselabproject.exceptions.department.DepartmentNotFoundException;
import com.example.caselabproject.exceptions.subscription.SubscriptionShouldChangeException;
import com.example.caselabproject.exceptions.user.DirectorIsNotLastException;
import com.example.caselabproject.exceptions.user.UserExistsException;
import com.example.caselabproject.exceptions.user.UserNotFoundException;
import com.example.caselabproject.models.DTOs.request.user.UserCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.user.UserUpdatePasswordRequest;
import com.example.caselabproject.models.DTOs.request.user.UserUpdateRequestDto;
import com.example.caselabproject.models.DTOs.response.DocumentGetAllResponse;
import com.example.caselabproject.models.DTOs.response.application.ApplicationFindResponseDto;
import com.example.caselabproject.models.DTOs.response.application.ApplicationItemGetByIdResponseDto;
import com.example.caselabproject.models.DTOs.response.user.*;
import com.example.caselabproject.models.entities.*;
import com.example.caselabproject.models.enums.ApplicationItemStatus;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.models.enums.SubscriptionName;
import com.example.caselabproject.repositories.*;
import com.example.caselabproject.services.MinioService;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
@Validated
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DocumentRepository DocumentRepository;
    private final RoleService roleService;
    private final DepartmentRepository departmentRepository;
    private final ApplicationRepository ApplicationRepository;
    private final ApplicationItemRepository applicationItemPageRepo;
    private final OrganizationRepository organizationRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final BillingLogRepository billingLogRepository;
    private final MinioService minioService;
    private final FileRepository fileRepository;

    @Override
    public UserGetByIdResponseDto getById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return UserGetByIdResponseDto.mapFromEntity(user);
    }

    @Override
    public UserGetByIdResponseDto getByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        return UserGetByIdResponseDto.mapFromEntity(user);
    }

    public boolean existById(Long id) {
        boolean exists = userRepository.existsById(id);
        if (!exists) {
            throw new UserNotFoundException(id);
        }
        return true;
    }

    @Override
    public UserAvatarResponseDto addAvatar(MultipartFile multipartFile, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        File avatarPath = user.getAvatarPath();
        avatarPath.setType(multipartFile.getContentType());
        avatarPath.setSize(multipartFile.getSize());
        avatarPath.setName(multipartFile.getName());
        avatarPath.setPath(minioService.saveFile("files", multipartFile));
        fileRepository.save(avatarPath);
        userRepository.save(user);
        return UserAvatarResponseDto.mapFromEntity(user);
    }


    @Override
    // если подходит количество юзеров то не меняем тариф
    public UserCreateResponseDto create(UserCreateRequestDto userRequestDto, String username) {
        User userAdmin = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        checkSubscription(userAdmin);

        User user = userRequestDto.mapToEntity();


        user.setRoles(roleService.findRolesByRoleDtoList(userRequestDto.getRoles()));
        user.getAuthUserInfo().setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        user.setAvatarPath(new File());
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
            user.setDepartment(departmentRepository.findById(userUpdateRequestDto.getDepartmentId())
                    .orElseThrow(() -> new DepartmentNotFoundException(userUpdateRequestDto.getDepartmentId())));
        }
        if (userUpdateRequestDto.getRoles() != null) {
            user.setRoles(roleService.findRolesByRoleDtoList(userUpdateRequestDto.getRoles()));
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
    public UserUpdateResponseDto updatePasswordById(Long id, UserUpdatePasswordRequest req) {
        User user = getUserById(id);
        user.getAuthUserInfo().setPassword(req.getPassword());
        return UserUpdateResponseDto.mapFromEntity(user);
    }

    @Override
    @Transactional
    public UserDeleteResponseDto deleteById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        if (user.getIsDirector()) {
            Long departmentIdOfDirector = user.getDepartment().getId();
            List<User> activeUsersOfDepartment = userRepository.findByIsDirectorAndRecordStateAndDepartment_Id(
                    false,
                    RecordState.ACTIVE,
                    departmentIdOfDirector);
            if (!activeUsersOfDepartment.isEmpty()) {
                throw new DirectorIsNotLastException(departmentIdOfDirector, id);
            }
        }
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
    public UserUpdateResponseDto appointDirector(Long departmentId, Long userId) {
        Optional<User> formerDirector = userRepository.findByIsDirectorAndDepartment_Id(true, departmentId);
        User newDirector = userRepository.findByIdAndDepartment_id(userId, departmentId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        newDirector.setIsDirector(true);
        if (formerDirector.isPresent()) {
            formerDirector.get().setIsDirector(false);
            List<ApplicationItem> activeApplicationItems = formerDirector.get().getApplicationItems()
                    .stream()
                    .filter(applicationItem -> applicationItem.getRecordState() == RecordState.ACTIVE
                            && applicationItem.getStatus() == ApplicationItemStatus.PENDING)
                    .toList();
            newDirector.getApplicationItems().addAll(activeApplicationItems);
        }
        userRepository.save(newDirector);
        return UserUpdateResponseDto.mapFromEntity(newDirector);
    }


    @Override
    @Transactional
    public List<DocumentGetAllResponse> findDocsByFiltersByPage(
            Long creatorId,
            String name,
            LocalDateTime creationDateFrom,
            LocalDateTime creationDateTo,
            Long documentConstructorTypeId,
            RecordState recordState,
            Pageable pageable) {
        if (existById(creatorId)) {
            List<Document> res;

            if (documentConstructorTypeId != null) {
                // Фильтр по documentConstructorTypeId, если он не null
                res = DocumentRepository
                        .findAllByCreator_idAndNameContainingIgnoreCaseAndCreationDateAfterAndCreationDateBeforeAndDocumentConstructorType_IdAndRecordState(
                                creatorId,
                                name,
                                creationDateFrom,
                                creationDateTo,
                                documentConstructorTypeId,
                                recordState,
                                pageable).toList();
            } else {
                // Если documentConstructorTypeId == null, игнорируем фильтр
                res = DocumentRepository
                        .findAllByCreator_idAndNameContainingIgnoreCaseAndCreationDateAfterAndCreationDateBeforeAndRecordState(
                                creatorId,
                                name,
                                creationDateFrom,
                                creationDateTo,
                                recordState,
                                pageable).toList();
            }

            List<DocumentGetAllResponse> documentCreateResponseDtoList = DocumentGetAllResponse
                    .mapFromListOfEntities(res);
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
                userRepository
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
            Department department = departmentRepository
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
    public List<ApplicationFindResponseDto> findApplicationsByCreatorIdByPage(Long id, RecordState recordState, Pageable pageable) {
        if (existById(id)) {
            return ApplicationFindResponseDto
                    .mapFromListEntity(ApplicationRepository.findAllByCreatorId_idAndRecordState(id, recordState, pageable).toList());
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
        userByUsername.getRoles().forEach(o -> {
            if (o.getName().equals("ROLE_ADMIN")) {
                isAdmin.set(true);
            }
        });
        User userById = getUserById(id);
        //Can be read only by admins, himself and from the same department
        if (!isAdmin.get() &&
                !userByUsername.getId().equals(id) &&
                !userById.getDepartment().getId().equals(userByUsername.getDepartment().getId())) {
            throw new ApplicationItemPermissionException();
        }
        Page<ApplicationItem> applicationItemPage = applicationItemPageRepo
                .findAllByToUser_idAndRecordStateAndApplication_NameContainsIgnoreCase(
                        id,
                        recordState,
                        applicationName,
                        pageable);
        List<ApplicationItem> res;
        if (status != null) {
            res = applicationItemPage.getContent()
                    .stream()
                    .filter(o -> o.getStatus().equals(status))
                    .toList();
        } else {
            res = applicationItemPage.getContent();
        }
        return res.stream()
                .map(ApplicationItemGetByIdResponseDto::mapFromEntity)
                .toList();
    }

    @Override
    public Long getOrganizationIdByEntityId(Long entityId) {
        return getUserById(entityId)
                .getOrganization().getId();
    }

    private User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        return user;
    }

    private User getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return user;
    }

    //вынести в преавторайз
    private void checkSubscription(User userAdmin) {
        Organization organization = userAdmin.getOrganization();
        Subscription currentSubscription = organization.getSubscription();
        Integer employees = organization.getEmployees().size();
        Integer amountOfPeopleInCurrentSubscription = currentSubscription.getAmountOfPeople();

        if (employees.equals(amountOfPeopleInCurrentSubscription))
            throw new SubscriptionShouldChangeException(amountOfPeopleInCurrentSubscription);


    }


}
