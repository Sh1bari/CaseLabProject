package com.example.caselabproject.exceptions.document;


import com.example.caselabproject.exceptions.GlobalAppException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DocumentCreateException extends GlobalAppException {
    public DocumentCreateException(String name) {
        super(500, "Can not create document " + name);
        log.warn(message);
    }
}
