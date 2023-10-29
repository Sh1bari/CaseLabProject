package com.example.caselabproject.exceptions;

public class FileNotExistException extends GlobalAppException {

    public FileNotExistException(Long id) {
        super(404, "File does not exist!");
    }
}
