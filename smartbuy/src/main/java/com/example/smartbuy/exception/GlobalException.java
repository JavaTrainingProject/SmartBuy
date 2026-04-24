package com.example.smartbuy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;



@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<String> handleCategoryAlreadyExists(CategoryAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
        
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Map<String, Object>> handleValidationException(
                MethodArgumentNotValidException ex) {

            Map<String, Object> error = new HashMap<>();
            error.put("timestamp", LocalDateTime.now());
            error.put("status", HttpStatus.BAD_REQUEST.value());
            error.put("error", "Validation Error");

            String message = ex.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .findFirst()
                    .map(e -> e.getDefaultMessage())
                    .orElse("Validation failed");

            error.put("message", message);

            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
            Map<String, Object> error = new HashMap<>();
            error.put("timestamp", LocalDateTime.now());
            error.put("status", HttpStatus.BAD_REQUEST.value());
            error.put("error", "Bad Request");
            error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<Map<String, Object>> handleGlobalException(Exception ex) {
            Map<String, Object> error = new HashMap<>();
            error.put("timestamp", LocalDateTime.now());
            error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            error.put("error", "Internal Server Error");
            error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFound(UserNotFoundException ex) {
        return new ResponseEntity<>(
                buildResponse(HttpStatus.NOT_FOUND, "User Not Found", ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    private Map<String, Object> buildResponse(HttpStatus httpStatus, String userNotFound, String message) {
        return Map.of();
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleUserExists(UserAlreadyExistsException ex) {
        return new ResponseEntity<>(
                buildResponse(HttpStatus.CONFLICT, "User Already Exists", ex.getMessage()),
                HttpStatus.CONFLICT
        );
    }

    }

