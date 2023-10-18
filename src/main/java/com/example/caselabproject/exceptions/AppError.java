package com.example.caselabproject.exceptions;

import lombok.Data;

import java.io.File;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.util.Date;

@Data
public class AppError {
    private int status;
    private String message;
    private Date timestamp;

    public AppError(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}
