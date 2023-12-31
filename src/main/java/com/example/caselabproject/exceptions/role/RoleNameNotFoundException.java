package com.example.caselabproject.exceptions.role;

import com.example.caselabproject.exceptions.GlobalAppException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RoleNameNotFoundException extends GlobalAppException {
    public RoleNameNotFoundException(String name) {
        super(404, "Can not find role with name " + name);
        log.warn(message);
    }
}
