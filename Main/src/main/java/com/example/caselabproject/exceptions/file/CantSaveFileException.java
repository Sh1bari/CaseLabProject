package com.example.caselabproject.exceptions.file;


import com.example.caselabproject.exceptions.GlobalAppException;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
public class CantSaveFileException extends GlobalAppException {
    public CantSaveFileException() {
        super(500, "Cant save file");
    }
}
