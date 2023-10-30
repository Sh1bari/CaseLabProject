package com.example.caselabproject.exceptions;

public class NoFilesPageFoundException extends GlobalAppException {

    public NoFilesPageFoundException(Integer page) {
        super(404, "No content found by page " + page);
    }
}
