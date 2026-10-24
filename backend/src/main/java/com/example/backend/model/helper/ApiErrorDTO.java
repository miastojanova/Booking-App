package com.example.backend.model.helper;

import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
public class ApiErrorDTO {
    private Instant timestamp;
    int status;
    String message;
    Map<String, String> fieldErrors;

    public ApiErrorDTO(Instant timestamp, int status, String message, Map<String, String> fieldErrors) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.fieldErrors = fieldErrors;
    }
}
