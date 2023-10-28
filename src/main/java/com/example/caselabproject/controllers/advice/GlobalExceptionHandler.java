package com.example.caselabproject.controllers.advice;

import com.example.caselabproject.exceptions.AppError;
import com.example.caselabproject.exceptions.GlobalAppException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            ConstraintViolationException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<AppError> onValidationExceptions(RuntimeException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new AppError(400, exception.getMessage()));
    }
    @ExceptionHandler({
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<AppError> onValidationExceptionsMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new AppError(400, exception.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }

    @ExceptionHandler({
            GlobalAppException.class
    })
    public ResponseEntity<AppError> documentConstructorTypeNameExistsException(GlobalAppException ex) {
        return ResponseEntity
                .status(ex.getStatus())
                .body(new AppError(ex.getStatus(), ex.getMessage()));
    }

}
