package com.example.smartbuy.exception;

import com.example.smartbuy.ApiResponse;
import com.example.smartbuy.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

        // 1. Validation Errors (@Valid)
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiResponse<String>> handleValidationException(
                MethodArgumentNotValidException ex) {

            String message = ex.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .findFirst()
                    .map(e -> e.getDefaultMessage())
                    .orElse("Validation failed");

            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(message, false, null));
        }

        // 2. Resource Not Found
        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ApiResponse<String>> handleNotFound(ResourceNotFoundException ex) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(ex.getMessage(), false, null));
        }

        // 3. Illegal Argument (e.g., invalid status)
        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ApiResponse<String>> handleIllegalArgument(IllegalArgumentException ex) {

            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(ex.getMessage(), false, null));
        }

        // 4. JSON Parsing Error (important for multipart + JSON)
        @ExceptionHandler(JsonProcessingException.class)
        public ResponseEntity<ApiResponse<String>> handleJsonError(JsonProcessingException ex) {

            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Invalid JSON format", false, null));
        }

        // 5. Runtime Exception (file upload, etc.)
        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<ApiResponse<String>> handleRuntime(RuntimeException ex) {

            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(ex.getMessage(), false, null));
        }

        // 6. Global Exception (ONLY ONE)
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiResponse<String>> handleGlobal(Exception ex) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Something went wrong", false, null));
        }
    }