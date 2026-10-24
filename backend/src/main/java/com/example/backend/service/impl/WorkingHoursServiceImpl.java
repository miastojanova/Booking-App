package com.example.backend.service.impl;

import com.example.backend.model.entity.DaySchedule;
import com.example.backend.model.helper.WorkingHoursDTO;
import com.example.backend.repository.DayScheduleRepository;
import com.example.backend.service.interfaces.WorkingHoursService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class WorkingHoursServiceImpl implements WorkingHoursService {
    private final DayScheduleRepository scheduleRepository;
    private final LocalTime defaultOpen;
    private final LocalTime defaultClose;

    public WorkingHoursServiceImpl(
            DayScheduleRepository scheduleRepository,
            @Value("${app.booking.default-open:09:00}")
            String defaultOpen,
            @Value("${app.booking.default-close:17:00}")
            String defaultClose
    ) {
        this.scheduleRepository = scheduleRepository;
        this.defaultOpen = LocalTime.parse(defaultOpen);
        this.defaultClose = LocalTime.parse(defaultClose);

        if (!this.defaultClose.isAfter(this.defaultOpen)) {
            throw new IllegalArgumentException(
                    "Default closing time must be after opening time."
            );
        }
    }

    @Override
    public WorkingHoursDTO getWorkingHours(LocalDate day) {
        Objects.requireNonNull(day, "Day is required.");

        DaySchedule schedule = scheduleRepository.findByDay(day)
                .orElse(null);

        if (schedule == null) {
            return new WorkingHoursDTO(
                    day,
                    defaultOpen,
                    defaultClose,
                    false
            );
        } //use default values if no override exists

        if (schedule.isClosed()) {
            return new WorkingHoursDTO(day, null, null, true);
        } //if the day is marked as closed

        LocalTime open = schedule.getOpenTime();
        LocalTime close = schedule.getCloseTime();

        if (open == null || close == null || !close.isAfter(open)) {
            throw new IllegalStateException(
                    "Invalid working hours for " + day
            );
        }

        return new WorkingHoursDTO(day, open, close, false);
    }

    @Override
    public WorkingHoursDTO updateWorkingHours(WorkingHoursDTO dto) {
        Objects.requireNonNull(dto.getDay(), "Day is required.");

        DaySchedule schedule = scheduleRepository.findByDay(dto.getDay())
                .orElse(new DaySchedule(dto.getDay(), defaultOpen, defaultClose, false));

        schedule.setClosed(dto.isClosed());
        if (!dto.isClosed()) {
            schedule.setOpenTime(dto.getOpenTime() != null ? dto.getOpenTime() : defaultOpen);
            schedule.setCloseTime(dto.getCloseTime() != null ? dto.getCloseTime() : defaultClose);
        }

        DaySchedule saved = scheduleRepository.save(schedule);
        return new WorkingHoursDTO(
                saved.getDay(),
                saved.getOpenTime(),
                saved.getCloseTime(),
                saved.isClosed()
        );
    }

    @Override
    public List<WorkingHoursDTO> getAllSchedules() {
        return scheduleRepository.findAll().stream()
                .map(s -> new WorkingHoursDTO(
                        s.getDay(),
                        s.getOpenTime(),
                        s.getCloseTime(),
                        s.isClosed()
                ))
                .collect(Collectors.toList());
    }
}
