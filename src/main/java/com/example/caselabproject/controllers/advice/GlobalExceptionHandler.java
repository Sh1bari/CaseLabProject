package com.example.caselabproject.controllers.advice;

import com.example.caselabproject.exceptions.AppError;
import com.example.caselabproject.exceptions.DocumentConstructorTypeNameExistsException;
import com.example.caselabproject.exceptions.DocumentCreateException;
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
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<AppError> onValidationExceptions(RuntimeException exception) {
        return ResponseEntity
                .badRequest()
                .body(new AppError(400, exception.getMessage()));
    }

    @ExceptionHandler({
            DocumentConstructorTypeNameExistsException.class
    })
    public ResponseEntity<AppError> documentConstructorTypeNameExistsException(DocumentConstructorTypeNameExistsException ex) {
        return ResponseEntity
                .unprocessableEntity()
                .body(new AppError(ex.getStatus(), ex.getMessage()));
    }
    @ExceptionHandler({
            DocumentCreateException.class
    })
    public ResponseEntity<AppError> documentCreateException(DocumentCreateException ex) {
        return ResponseEntity
                .unprocessableEntity()
                .body(new AppError(ex.getStatus(), ex.getMessage()));
    }

}
