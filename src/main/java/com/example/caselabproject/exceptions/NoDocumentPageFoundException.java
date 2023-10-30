package com.example.caselabproject.exceptions;

public class NoDocumentPageFoundException extends GlobalAppException {
    public NoDocumentPageFoundException(Integer page) {
        super(404, "No content found by page " + page);
    }
}
