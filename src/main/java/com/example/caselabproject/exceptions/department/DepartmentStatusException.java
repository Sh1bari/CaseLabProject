package com.example.caselabproject.exceptions.department;


import com.example.caselabproject.exceptions.GlobalAppException;
import com.example.caselabproject.models.enums.RecordState;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DepartmentStatusException extends GlobalAppException {

    public DepartmentStatusException(Long departmentId, RecordState recordState) {
        super(409, "Department " + departmentId + " already has the status " + recordState.toString());
        log.warn(message);
    }
}