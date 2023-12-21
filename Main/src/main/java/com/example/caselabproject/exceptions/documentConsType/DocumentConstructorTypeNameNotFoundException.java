package com.example.caselabproject.exceptions.documentConsType;

import com.example.caselabproject.exceptions.GlobalAppException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DocumentConstructorTypeNameNotFoundException extends GlobalAppException {

    public DocumentConstructorTypeNameNotFoundException(String name) {
        super(404, "DocumentConstructorType " + name + " does not exist");
        log.warn(message);
    }
}
