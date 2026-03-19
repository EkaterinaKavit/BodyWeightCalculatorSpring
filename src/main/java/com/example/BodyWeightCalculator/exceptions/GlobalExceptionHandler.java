package com.example.BodyWeightCalculator.exceptions;


import jakarta.el.MethodNotFoundException;
import org.apache.catalina.util.ResourceSet;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleValidationExceptions(MethodArgumentNotValidException exceptions){
        Map<String,String> errors = new HashMap<>();
        exceptions.getBindingResult().getAllErrors().forEach((error)-> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName,errorMessage);

        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleResourceNotFoundException(ResourceNotFoundException exceptions){
        Map<String,String> errors = new HashMap<>();
        errors.put("error", exceptions.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Map<String,String>> handleGenericException(Exception exception){
//        Map<String,String> errors = new HashMap<>();
//        errors.put("error", "произошла внутренняя ошибка сервера");
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
//    }
}
