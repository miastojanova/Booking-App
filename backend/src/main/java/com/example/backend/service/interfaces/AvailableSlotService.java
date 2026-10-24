package com.example.backend.service.interfaces;

import com.example.backend.model.entity.Booking;
import com.example.backend.model.helper.AvailableSlotDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AvailableSlotService {
    List<AvailableSlotDTO> getAvailableSlots(Long serviceId, LocalDate day);
    boolean bookingOverlapsCheck(LocalDateTime start, LocalDateTime end, List<Booking> bookings);

}
