package com.example.backend.repository;
import com.example.backend.model.entity.DaySchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DayScheduleRepository
        extends JpaRepository<DaySchedule, Long> {

    Optional<DaySchedule> findByDay(LocalDate day);
}
