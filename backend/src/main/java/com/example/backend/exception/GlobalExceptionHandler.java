package com.example.backend.exception;

import com.example.backend.model.helper.ApiErrorDTO;

import jakarta.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookingConflictException.class)
    public ResponseEntity<ApiErrorDTO> handleBookingConflict(
            BookingConflictException exception
    ) {
        return error(
                HttpStatus.CONFLICT.value(),
                exception.getMessage(),
                Map.of()
        );
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiErrorDTO> handleResponseStatus(
            ResponseStatusException exception
    ) {
        String message = exception.getReason() != null
                ? exception.getReason()
                : "The request could not be completed.";

        return error(
                exception.getStatusCode().value(),
                message,
                Map.of()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDTO> handleRequestValidation(
            MethodArgumentNotValidException exception
    ) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError ->
                        fieldErrors.putIfAbsent(
                                fieldError.getField(),
                                fieldError.getDefaultMessage() != null
                                        ? fieldError.getDefaultMessage()
                                        : "Invalid value."
                        )
                );

        return error(
                HttpStatus.BAD_REQUEST.value(),
                "Please correct the highlighted fields.",
                fieldErrors
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorDTO> handleServiceValidation(
            ConstraintViolationException exception
    ) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();

        exception.getConstraintViolations()
                .forEach(violation ->
                        fieldErrors.putIfAbsent(
                                violation.getPropertyPath().toString(),
                                violation.getMessage()
                        )
                );

        return error(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid request values.",
                fieldErrors
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiErrorDTO> handleMissingParameter(
            MissingServletRequestParameterException exception
    ) {
        return error(
                HttpStatus.BAD_REQUEST.value(),
                "Missing required parameter: "
                        + exception.getParameterName(),
                Map.of()
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorDTO> handleTypeMismatch(
            MethodArgumentTypeMismatchException exception
    ) {
        return error(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid value for parameter: "
                        + exception.getName(),
                Map.of()
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorDTO> handleInvalidBody(
            HttpMessageNotReadableException exception
    ) {
        return error(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid request body. Check the JSON structure, "
                        + "date format (yyyy-MM-dd), and time format (HH:mm).",
                Map.of()
        );
    }

    private ResponseEntity<ApiErrorDTO> error(
            int status,
            String message,
            Map<String, String> fieldErrors
    ) {
        ApiErrorDTO body = new ApiErrorDTO(
                Instant.now(),
                status,
                message,
                fieldErrors
        );

        return ResponseEntity.status(status).body(body);
    }
}
