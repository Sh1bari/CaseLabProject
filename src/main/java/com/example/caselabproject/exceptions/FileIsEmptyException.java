package com.example.caselabproject.exceptions;

public class FileIsEmptyException extends GlobalAppException {

    public FileIsEmptyException(String name) {
        super(400, "File " + name + " is empty");
    }
}
