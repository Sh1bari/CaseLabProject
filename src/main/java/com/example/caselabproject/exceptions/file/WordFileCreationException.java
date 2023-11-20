package com.example.caselabproject.exceptions.file;

import com.example.caselabproject.exceptions.GlobalAppException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WordFileCreationException extends GlobalAppException {

    public WordFileCreationException(Long documentId) {
        super(500, "Could not create word file for document with id: " + documentId);
        log.warn(message);
    }
}
