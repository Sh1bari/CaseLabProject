package com.example.caselabproject.exceptions.biling;

import com.example.caselabproject.exceptions.GlobalAppException;

public class PdfCreatingException extends GlobalAppException {
    public PdfCreatingException(String message) {
        super(400, message);
    }
}
