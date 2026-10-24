package com.example.backend.controller;

import com.example.backend.model.helper.BookingDTO;
import com.example.backend.model.helper.CreateBookingDTO;
import com.example.backend.service.interfaces.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<BookingDTO> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(
            @Valid @RequestBody CreateBookingDTO request
    ) {
        BookingDTO booking =
                bookingService.createBooking(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(booking);
    }
}
