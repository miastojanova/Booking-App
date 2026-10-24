package com.example.backend.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Entity
@Table(name = "day_schedule")
public class DaySchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "day", nullable = false, unique = true)
    private LocalDate day;

    @Setter
    @Column(name = "open_time")
    private LocalTime openTime;

    @Setter
    @Column(name = "close_time")
    private LocalTime closeTime;

    @Setter
    @Column(name = "closed", nullable = false)
    private boolean closed;

    protected DaySchedule() {
    }

    public DaySchedule(
            LocalDate day,
            LocalTime openTime,
            LocalTime closeTime,
            boolean closed
    ) {
        this.day = day;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.closed = closed;
    }

}
