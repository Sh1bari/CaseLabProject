package com.example.caselabproject.exceptions.file;

import com.example.caselabproject.exceptions.GlobalAppException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileConnectToDocumentException extends GlobalAppException {

    public FileConnectToDocumentException() {
        super(500, "Could not connect file to document");
        log.warn(message);
    }
}
