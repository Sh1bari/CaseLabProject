package com.example.caselabproject.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileNotExistException extends GlobalAppException {

    public FileNotExistException(Long id) {
        super(404, "File does not exist!");
        log.warn(message);
    }
}
