package com.example.backend.model.helper;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class WorkingHoursDTO {
    private LocalDate day;
    private LocalTime openTime;
    private LocalTime closeTime;
    private boolean closed;

    public WorkingHoursDTO(LocalDate day, LocalTime openTime, LocalTime closeTime, boolean closed) {
        this.day = day;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.closed = closed;
    }
}
