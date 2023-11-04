package com.example.caselabproject.exceptions;

public class UpdateApplicationStatusException extends GlobalAppException{
    //TODO изменить статус ошибки
    public UpdateApplicationStatusException() {
        super(400, "application status cannot be changed");
    }
}
