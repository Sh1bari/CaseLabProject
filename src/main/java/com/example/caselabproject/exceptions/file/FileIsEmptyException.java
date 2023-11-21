package com.example.caselabproject.exceptions.file;

import com.example.caselabproject.exceptions.GlobalAppException;

public class FileIsEmptyException extends GlobalAppException {

    public FileIsEmptyException(String name) {
        super(400, "File " + name + " is empty");
    }
}
