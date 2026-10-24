package com.example.backend.model.helper;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CreateBookingDTO {
    private Long id;
    private LocalDate day;
    private LocalTime startTime;
    private String name;
    private String email;
    private String phone;
}
