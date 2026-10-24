package com.example.backend.service.impl;

import com.example.backend.model.entity.Booking;
import com.example.backend.model.entity.SalonService;
import com.example.backend.model.helper.AvailableSlotDTO;
import com.example.backend.model.helper.WorkingHoursDTO;
import com.example.backend.repository.BookingRepository;
import com.example.backend.repository.SalonServiceRepository;
import com.example.backend.service.interfaces.AvailableSlotService;
import com.example.backend.service.interfaces.WorkingHoursService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AvailableSlotServiceImpl implements AvailableSlotService {

    private final SalonServiceRepository serviceRepository;
    private final BookingRepository bookingRepository;
    private final WorkingHoursService workingHoursService;
    private final Clock clock;
    private final int stepMinutes;

    public AvailableSlotServiceImpl(SalonServiceRepository serviceRepository,
                                    BookingRepository bookingRepository,
                                    WorkingHoursService workingHoursService,
                                    Clock clock,
                                    @Value("${app.booking.slot-step-minutes:15}") int stepMinutes) {
        if (stepMinutes <= 0) {
            throw new IllegalArgumentException(
                    "Slot step must be greater than zero."
            );
        }

        this.serviceRepository = serviceRepository;
        this.bookingRepository = bookingRepository;
        this.workingHoursService = workingHoursService;
        this.clock = clock;
        this.stepMinutes = stepMinutes;
    }

    @Override
    public List<AvailableSlotDTO> getAvailableSlots(Long serviceId, LocalDate day) {
        if (serviceId == null || day == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Service and date are required."
            );
        }

        SalonService service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Service not found."
                ));

        LocalDateTime now = LocalDateTime.now(clock);

        if (day.isBefore(now.toLocalDate())) {
            return List.of();
        }

        WorkingHoursDTO hours =
                workingHoursService.getWorkingHours(day);

        if (hours.isClosed()) {
            return List.of();
        }

        LocalDateTime opening = day.atTime(hours.getOpenTime());
        LocalDateTime closing = day.atTime(hours.getCloseTime());

        List<Booking> bookings = bookingRepository.findOverlapping(
                opening,
                closing
        );

        List<AvailableSlotDTO> available = new ArrayList<>();

        for (
                LocalDateTime start = opening;
                !start.plusMinutes(service.getDurationMinutes())
                        .isAfter(closing);
                start = start.plusMinutes(stepMinutes)
        ) {
            LocalDateTime end =
                    start.plusMinutes(service.getDurationMinutes());

            if (!start.isAfter(now)) {
                continue;
            }

            if (!bookingOverlapsCheck(start, end, bookings)) {
                available.add(new AvailableSlotDTO(start, end));
            }
        }
        return available;
    }

    @Override
    public boolean bookingOverlapsCheck(LocalDateTime start, LocalDateTime end, List<Booking> bookings) {
        return bookings.stream().anyMatch(booking ->
                start.isBefore(booking.getEndAt())
                        && end.isAfter(booking.getStartAt())
        );
    }
}
