package com.example.backend.service.impl;

import com.example.backend.exception.BookingConflictException;
import com.example.backend.model.entity.Booking;
import com.example.backend.model.entity.Client;
import com.example.backend.model.entity.SalonService;
import com.example.backend.model.helper.BookingDTO;
import com.example.backend.model.helper.CreateBookingDTO;
import com.example.backend.model.helper.WorkingHoursDTO;
import com.example.backend.repository.BookingRepository;
import com.example.backend.repository.ClientRepository;
import com.example.backend.repository.SalonServiceRepository;
import com.example.backend.service.interfaces.BookingService;
import com.example.backend.service.interfaces.WorkingHoursService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class BookingServiceImpl implements BookingService {

    private final SalonServiceRepository serviceRepository;
    private final ClientRepository clientRepository;
    private final BookingRepository bookingRepository;
    private final WorkingHoursService workingHoursService;
    private final Clock clock;
    private final int stepMinutes;

    public BookingServiceImpl(SalonServiceRepository serviceRepository,
                              ClientRepository clientRepository,
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
        this.clientRepository = clientRepository;
        this.bookingRepository = bookingRepository;
        this.workingHoursService = workingHoursService;
        this.clock = clock;
        this.stepMinutes = stepMinutes;
    }


    @Override
    @Transactional
    public BookingDTO createBooking(CreateBookingDTO dto) {
        SalonService service =
                serviceRepository.findById(dto.getId())
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Service not found."
                        ));

        LocalDateTime startAt =
                dto.getDay().atTime(dto.getStartTime());

        LocalDateTime endAt =
                startAt.plusMinutes(service.getDurationMinutes());

        LocalDateTime now = LocalDateTime.now(clock);

        if (!startAt.isAfter(now)) {
            throw badRequest("Booking must start in the future.");
        }

        WorkingHoursDTO hours =
                workingHoursService.getWorkingHours(dto.getDay());

        if (hours.isClosed()) {
            throw new BookingConflictException(
                    "The salon is closed on the selected date."
            );
        }

        LocalDateTime opening =
                dto.getDay().atTime(hours.getOpenTime());

        LocalDateTime closing =
                dto.getDay().atTime(hours.getCloseTime());

        if (startAt.isBefore(opening) || endAt.isAfter(closing)) {
            throw badRequest(
                    "The appointment must fit inside working hours."
            );
        }

        validateStartingTime(startAt, opening);

        if (bookingRepository.countOverlapping(startAt, endAt) > 0) {
            throw new BookingConflictException(
                    "This time slot is no longer available."
            );
        }

        Client client = new Client(
                dto.getName().strip(),
                dto.getEmail().strip(),
                dto.getPhone().strip()
        );

        clientRepository.save(client);

        Booking booking = new Booking(
                service,
                client,
                startAt,
                endAt
        );

        try {
            Booking saved = bookingRepository.saveAndFlush(booking);

            return new BookingDTO(
                    saved.getId(),
                    service.getId(),
                    service.getName(),
                    client.getName(),
                    saved.getStartAt(),
                    saved.getEndAt()
            );
        } catch (DataIntegrityViolationException exception) {
            if (isExclusionViolation(exception)) {
                throw new BookingConflictException(
                        "This time slot was just booked. Choose another time."
                );
            }

            throw exception;
        }
    }

    @Override
    @Transactional
    public List<BookingDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(b -> new BookingDTO(
                        b.getId(),
                        b.getService().getId(),
                        b.getService().getName(),
                        b.getClient().getName(),
                        b.getStartAt(),
                        b.getEndAt()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void validateStartingTime(LocalDateTime startTime, LocalDateTime opening) {

    }

    private ResponseStatusException badRequest(String message) {
        return new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                message
        );
    }

    private boolean isExclusionViolation(Throwable exception) {
        for (
                Throwable cause = exception;
                cause != null;
                cause = cause.getCause()
        ) {
            if (cause instanceof SQLException sqlException) {
                for (
                        SQLException current = sqlException;
                        current != null;
                        current = current.getNextException()
                ) {
                    if ("23P01".equals(current.getSQLState())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

}
