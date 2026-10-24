package com.example.backend.service.interfaces;

import com.example.backend.model.helper.BookingDTO;
import com.example.backend.model.helper.CreateBookingDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    BookingDTO createBooking(CreateBookingDTO dto);
    List<BookingDTO> getAllBookings();
    void validateStartingTime(LocalDateTime startTime, LocalDateTime opening);

}
