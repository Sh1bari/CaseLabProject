package com.example.caselabproject.exceptions;

public class FileAddException extends GlobalAppException {

    public FileAddException() {
        super(500, "Could not add file");
    }
}
