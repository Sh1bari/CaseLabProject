package com.example.caselabproject.exceptions;

import lombok.*;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
public class DeletedUserException extends GlobalAppException{
    public DeletedUserException(String username) {
        super(403, "User "+ username +" has been banned");
    }
}
