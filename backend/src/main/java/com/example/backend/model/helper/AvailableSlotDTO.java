package com.example.backend.model.helper;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AvailableSlotDTO {
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public AvailableSlotDTO(LocalDateTime start, LocalDateTime end) {
        this.startTime = start;
        this.endTime = end;
    }
}
