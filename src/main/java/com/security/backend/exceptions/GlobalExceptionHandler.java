package com.security.backend.exceptions;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.security.backend.dtos.Response.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

        // private final Logger logger =
        // LoggerFactory.getLogger(GlobalExceptionHandler.class);

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException ex,
                        HttpServletRequest request) {
                Map<String, String> validationErrors = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .collect(Collectors.toMap(
                                                FieldError::getField,
                                                error -> error.getDefaultMessage() == null ? "Message not defined."
                                                                : error.getDefaultMessage(),
                                                (existing, replacement) -> existing + "; " + replacement));

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failure(
                                request.getRequestURI(), "Data validation failed.", 400, validationErrors));
        }

        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<ApiResponse<Object>> handleBadCredentialsException(BadCredentialsException ex,
                        HttpServletRequest request) {
                return ApiResponse.failure(request.getRequestURI(), ex.getMessage(), 400, null);
        }

}