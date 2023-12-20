package com.example.caselabproject.preAuthValidations;

import com.example.caselabproject.exceptions.department.*;
import com.example.caselabproject.exceptions.user.UserNotFoundException;
import com.example.caselabproject.models.DTOs.request.department.DepartmentChildDto;
import com.example.caselabproject.models.entities.Department;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.repositories.DepartmentRepository;
import com.example.caselabproject.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class DepartmentSecurityService {

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    public boolean canDeleteDepartment(String username, Long departmentId) {
        User user = getUserByUserName(username);
        Department department = getDepartmentById(departmentId);
//        AtomicBoolean isAdmin = new AtomicBoolean(false);
//        user.getRoles().forEach(o -> {
//            if (o.getName().equals("ROLE_ADMIN")) {
//                isAdmin.set(true);
//            }
//        });
        boolean canDeleteDepartment = true;
        if (!department.getOrganization().equals(user.getOrganization()) /*|| !isAdmin.get()*/ || !user.getIsDirector()) {
            canDeleteDepartment = false;
        } else {
            if (department.getRecordState().equals(RecordState.DELETED)) {
                throw new DepartmentStatusException(departmentId, RecordState.DELETED);
            }
        }
        return canDeleteDepartment;
    }

    public boolean canRecoverDepartment(String username, Long departmentId) {
        User user = getUserByUserName(username);
        Department department = getDepartmentById(departmentId);
//        AtomicBoolean isAdmin = new AtomicBoolean(false);
//        user.getRoles().forEach(o -> {
//            if (o.getName().equals("ROLE_ADMIN")) {
//                isAdmin.set(true);
//            }
//        });
        boolean canRecoverDepartment = true;
        if (!department.getOrganization().equals(user.getOrganization()) /*|| !isAdmin.get()*/ || !user.getIsDirector()) {
            canRecoverDepartment = false;
        } else {
            if (department.getRecordState().equals(RecordState.ACTIVE)) {
                throw new DepartmentStatusException(departmentId, RecordState.ACTIVE);
            }
        }
        return canRecoverDepartment;
    }

    public boolean canUpdateDepartment(String username, Long departmentId) {
        User user = getUserByUserName(username);
        Department department = getDepartmentById(departmentId);
//        AtomicBoolean isAdmin = new AtomicBoolean(false);
//        user.getRoles().forEach(o -> {
//            if (o.getName().equals("ROLE_ADMIN")) {
//                isAdmin.set(true);
//            }
//        });
        boolean canUpdateDepartment = true;
        if (!department.getOrganization().equals(user.getOrganization()) /*|| !isAdmin.get()*/ || !user.getIsDirector() || !department.getId().equals(user.getDepartment().getId())) {
            canUpdateDepartment = false;
        } else {
            if (department.getRecordState().equals(RecordState.ACTIVE)) {
                throw new DepartmentStatusException(departmentId, RecordState.ACTIVE);
            }
        }
        return canUpdateDepartment;
    }

    public boolean canGetDepartmentById(String username, Long departmentId) {
        User user = getUserByUserName(username);
        Department department = getDepartmentById(departmentId);

        boolean canGetDepartmentById = true;
        if (!department.getOrganization().equals(user.getOrganization())) {
            canGetDepartmentById = false;
        } else {
            if (department.getRecordState().equals(RecordState.ACTIVE)) {
                throw new DepartmentStatusException(departmentId, RecordState.ACTIVE);
            }
        }
        return canGetDepartmentById;
    }

    public boolean canGetApplicationItemsById(String username, Long departmentId) {
        User user = getUserByUserName(username);
        Department department = getDepartmentById(departmentId);
        AtomicBoolean isAdmin = new AtomicBoolean(false);
        user.getRoles().forEach(o -> {
            if (o.getName().equals("ROLE_ADMIN")) {
                isAdmin.set(true);
            }
        });
        boolean canGetApplicationItemsById = true;
        if (!department.getOrganization().equals(user.getOrganization()) || !isAdmin.get() || !department.getId().equals(user.getDepartment().getId())) {
            canGetApplicationItemsById = false;
        } else {
            if (department.getRecordState().equals(RecordState.ACTIVE)) {
                throw new DepartmentStatusException(departmentId, RecordState.ACTIVE);
            }
        }
        return canGetApplicationItemsById;
    }

    public boolean canAddChildDepartment(String username, Long parenDepartmentId, DepartmentChildDto departmentChildDto) {
        User user = getUserByUserName(username);
        Department parentDepartment = getDepartmentById(parenDepartmentId);
        List<Department> parentChildDepartments = parentDepartment.getChildDepartments();

        Department childDepartment = getDepartmentById(departmentChildDto.getId());

        checkValid(childDepartment, parentDepartment, parenDepartmentId, parentChildDepartments, departmentChildDto);

        boolean canAddChildDepartment = true;
        if (!parentDepartment.getOrganization().equals(user.getOrganization()) || !childDepartment.getOrganization().equals(user.getOrganization())
                || !parentDepartment.getOrganization().equals(childDepartment.getOrganization())
                || !parentDepartment.getId().equals(user.getDepartment().getId()) || !user.getIsDirector()) {
            canAddChildDepartment = false;
        } else {
            if (parentDepartment.getRecordState().equals(RecordState.ACTIVE) ||
                    childDepartment.getRecordState().equals(RecordState.ACTIVE)) {
                throw new DepartmentStatusException(departmentChildDto.getId(), RecordState.ACTIVE);
            }
        }
        return canAddChildDepartment;
    }


    private User getUserByUserName(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    private Department getDepartmentById(Long departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId));
    }

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
}
