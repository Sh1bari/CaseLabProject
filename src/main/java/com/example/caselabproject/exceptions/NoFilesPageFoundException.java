package com.example.caselabproject.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NoFilesPageFoundException extends GlobalAppException {

    public NoFilesPageFoundException(Integer page) {
        super(404, "No content found by page " + page);
        log.warn(message);
    }
}
