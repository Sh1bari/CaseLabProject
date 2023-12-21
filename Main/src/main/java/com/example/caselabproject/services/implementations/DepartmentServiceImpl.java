package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.applicationItem.ApplicationItemPermissionException;
import com.example.caselabproject.exceptions.department.*;
import com.example.caselabproject.exceptions.user.UserNotFoundException;
import com.example.caselabproject.models.DTOs.request.department.DepartmentChildDto;
import com.example.caselabproject.models.DTOs.request.department.DepartmentCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.department.DepartmentRequestDto;
import com.example.caselabproject.models.DTOs.response.application.ApplicationItemGetByIdResponseDto;
import com.example.caselabproject.models.DTOs.response.department.*;
import com.example.caselabproject.models.DTOs.response.user.UserGetByIdResponseDto;
import com.example.caselabproject.models.entities.ApplicationItem;
import com.example.caselabproject.models.entities.Department;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.ApplicationItemStatus;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.repositories.ApplicationItemPageRepository;
import com.example.caselabproject.repositories.DepartmentRepository;
import com.example.caselabproject.repositories.UserRepository;
import com.example.caselabproject.services.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;


@Service
@Validated
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final UserRepository userRepository;
    private final ApplicationItemPageRepository applicationItemPageRepo;
    private final UserRepository userRepo;

    @Override
    public DepartmentCreateResponseDto create(DepartmentCreateRequestDto requestDto, String username) {

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        Department department = requestDto.mapToEntity();
        department.setOrganization(user.getCreatedOrganization());

        Department saveDepartment = saveInternal(department);
        saveDepartment.setSerialKey(generateUniqueSerialKey(saveDepartment));

        return DepartmentCreateResponseDto.mapFromEntity(saveDepartment);
    }

    @Override
    public DepartmentGetByIdResponseDto setParentDepartment(Long parenDepartmentId, DepartmentChildDto departmentChildDto) {

        Department parentDepartment = findDepartmentById(parenDepartmentId);
        List<Department> parentChildDepartments = parentDepartment.getChildDepartments();

        Department childDepartment = findDepartmentById(departmentChildDto.getId());

        checkValid(childDepartment, parentDepartment, parenDepartmentId, parentChildDepartments, departmentChildDto);

        childDepartment.setParentDepartment(parentDepartment);

        parentChildDepartments.add(childDepartment);

        return DepartmentGetByIdResponseDto.mapFromEntity(parentDepartment);
    }


    @Override
    public DepartmentUpdateResponseDto updateName(Long departmentId, DepartmentRequestDto requestDto) {

        Department department = findDepartmentById(departmentId);

        department.setName(requestDto.getName());

        return DepartmentUpdateResponseDto.mapFromEntity(saveInternal(department));

    }


    @Override
    public DepartmentDeleteRecoverResponseDto deleteDepartment(Long departmentId) {
        Department department = findDepartmentById(departmentId);

        if (department.getRecordState().equals(RecordState.DELETED)) {
            throw new DepartmentStatusException(departmentId, RecordState.DELETED);
        }

        department.setRecordState(RecordState.DELETED);
        departmentRepository.save(department);
        return DepartmentDeleteRecoverResponseDto.mapFromEntity(department);
    }

    @Override
    public DepartmentDeleteRecoverResponseDto recoverDepartment(Long departmentId) {
        Department department = findDepartmentById(departmentId);

        if (department.getRecordState().equals(RecordState.ACTIVE)) {
            throw new DepartmentStatusException(departmentId, RecordState.ACTIVE);
        }

        department.setRecordState(RecordState.ACTIVE);
        departmentRepository.save(department);
        return DepartmentDeleteRecoverResponseDto.mapFromEntity(department);
    }


    @Override
    public DepartmentGetByIdResponseDto getById(Long departmentId) {
        Department department = findDepartmentById(departmentId);

        return DepartmentGetByIdResponseDto.mapFromEntity(department);
    }


    @Override
    public Page<DepartmentGetAllResponseDto> getAllDepartmentsPageByPage(Pageable pageable, String name, RecordState recordState,
                                                                         String serialKey, String username) {

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        Page<Department> departments =
                departmentRepository.findDepartmentsByNameContainingAndRecordStateAndSerialKeyAndOrganization(name, pageable, recordState, serialKey, user.getCreatedOrganization());

        return departments.map(DepartmentGetAllResponseDto::mapFromEntity);
    }


    @Override
    public Page<UserGetByIdResponseDto> getAllUsersFilteredByDepartment(RecordState recordState, Long departmentId, Pageable pageable, String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        Page<UserGetByIdResponseDto> users = userRepository.findByRecordStateAndDepartment_IdAndOrganization(recordState, pageable, departmentId, user.getCreatedOrganization())
                .map(UserGetByIdResponseDto::mapFromEntity);

        return users;
    }

    @Override
    public Long getOrganizationIdByEntityId(Long entityId) {
        return findDepartmentById(entityId)
                .getOrganization().getId();
    }

    @Override
    public List<ApplicationItemGetByIdResponseDto> findApplicationItemsByDepartmentIdByPage(
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
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(id));
        //Can be read only by admins, from the same department
        if (!isAdmin.get() &&
                !userByUsername.getDepartment().getId().equals(id)) {
            throw new ApplicationItemPermissionException();
        }

        Page<ApplicationItem> applicationItemPage = applicationItemPageRepo
                .findAllByToDepartment_idAndRecordStateAndApplication_NameContainsIgnoreCase(
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

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    private Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(id));
    }

    private boolean departmentIsActive(Department department) {
        if (department.getRecordState().equals(RecordState.DELETED)) {
            throw new DepartmentDeletedException(department.getId());
        }
        return true;
    }

    /**
     * Внутренний метод, позволяющий проверить возможность связывания наших департаментов.
     * <p>
     * Первая проверка на то, что пользователь не передал два одинаковых Id.
     * <p>
     * Вторая на то, что пользователь не пытается привязать родительский департамент к дочернему
     * и так же не пытается привязать родительский к дочерним департаментам своего дочернего департамента.
     * <p>
     * Третья не дает повторно привязывать департамент, если это было уже ранее сделано.
     * <p>
     * Четвертая на то, что бы мы не пытались перезаписать родительский департамент, если он уже есть.
     */
    private void checkValid(Department childDepartment, Department parentDepartment, Long parenDepartmentId,
                            List<Department> parentChildDepartments, DepartmentChildDto departmentChildDto) {

        if (childDepartment == parentDepartment)
            throw new DepartmentChildParentException();


        Department parentParentDep = parentDepartment.getParentDepartment();

        while (parentParentDep != null) {
            if (parentParentDep.getId().equals(childDepartment.getId()))
                throw new DepartmentChildParentException(childDepartment.getId(), parenDepartmentId);

            parentParentDep = parentParentDep.getParentDepartment();
        }

        if (parentChildDepartments.contains(childDepartment))
            throw new DepartmentChildException(departmentChildDto.getId(), parenDepartmentId);

        if (childDepartment.getParentDepartment() != null)
            throw new DepartmentParentException();
    }


    /**
     * Внутренний метод, позволяющий сохранить Department. Используется
     * для избежания повторов кода.
     */
    private Department saveInternal(Department department) {
        try {
            return departmentRepository.save(department);
        } catch (DataIntegrityViolationException ex) {
            throw new DepartmentSQLValidationException(department.getName());
        }
    }

    /**
     * Внутренний метод, позволяющий найти отдел по ID
     * для избежания повторов кода.
     */
    private Department findDepartmentById(Long departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId));
    }


    /**
     * Внутренний метод, позволяющий сгенерировать уникальный номер отдела.
     */
    private String generateUniqueSerialKey(Department department) {
        Random random = new Random();
        StringBuilder serialKey = new StringBuilder(3);

        for (int i = 0; i < 3; i++) {
            // Генерируем случайное число от 0 до 25 (для 26 букв алфавита)
            char randomChar = (char) ('a' + random.nextInt(26));
            serialKey.append(randomChar);
        }

        // Добавляем id в конец через дефис
        serialKey.append('-').append(department.getId());

        return serialKey.toString();
    }

}
